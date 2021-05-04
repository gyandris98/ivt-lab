package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import javax.swing.text.View;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore primary;
  private TorpedoStore secondary;

  @BeforeEach
  public void init(){
    this.primary = mock(TorpedoStore.class);
    this.secondary = mock(TorpedoStore.class);
    this.ship = new GT4500(primary, secondary);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(primary.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(primary.fire(1)).thenReturn(true);
    when(secondary.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_Primary() {
    // Arrange
    when(primary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_Single_Second_Secondary() {
    // Arrange
    when(secondary.fire(1)).thenReturn(true);
    when(primary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, times(1)).fire(1);
  }
  @Test
  public void fireTorpedo_Single_NoSwitchIfEmpty() {
    // Arrange
    when(primary.fire(1)).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(primary, times(2)).fire(1);
    verify(secondary, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_Single_NoFireIfEmpty() {
    // Arrange
    when(primary.isEmpty()).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    assertEquals(false, result);
    verify(primary, times(0)).fire(1);
    verify(secondary, times(0)).fire(1);

  }
  @Test
  public void fireTorpedo_Single_NoRetryIfFailure() {
    when(primary.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(false, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_All_NoFireIfEmpty() {
    // Arrange
    when(primary.isEmpty()).thenReturn(false);
    when(secondary.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(false, result);
    verify(primary, times(0)).fire(1);
    verify(secondary, times(0)).fire(1);
  }
  
  @Test
  public void fireTorpedo_Single_FireSecondaryAgain() {
    // Arrange
    when(secondary.fire(1)).thenReturn(true);
    when(primary.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(secondary, times(2)).fire(1);
    verify(primary, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_Single_BothEmpty() {
    // Arrange
    when(primary.isEmpty()).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

     // Assert
     assertEquals(false, result);
     verify(secondary, times(0)).fire(1);
    verify(primary, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_All_SecondaryEmpty() {
    // Arrange
    when(primary.isEmpty()).thenReturn(false);
    when(secondary.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
    verify(secondary, times(0)).fire(1);
   verify(primary, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_Single_FirePrimaryAgain() {
    // Arrange
    when(primary.fire(1)).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(primary, times(2)).fire(1);
    verify(secondary, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_All_PrimaryFailure() {
    // Arrange
    when(primary.isEmpty()).thenReturn(false);
    when(secondary.isEmpty()).thenReturn(false);
    when(primary.fire(1)).thenReturn(false);
    when(secondary.fire(1)).thenReturn(true);


    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
  }
  @Test
  public void fireTorpedo_All_SecondaryFailure() {
    // Arrange
    when(secondary.isEmpty()).thenReturn(false);
    when(primary.isEmpty()).thenReturn(false);
    when(secondary.fire(1)).thenReturn(false);
    when(primary.fire(1)).thenReturn(true);


    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
  }
  @Test
  public void fireTorpedo_All_NoFireIfAllEmpty() {
    // Arrange
    when(primary.isEmpty()).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
    verify(primary, times(0)).fire(1);
    verify(secondary, times(0)).fire(1);
  }
  @Test public void fireTorpedo_Single_NoFireIfPrimaryEmptyAfterFire() {
    // Arrange
    when(primary.fire(1)).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    when(primary.isEmpty()).thenReturn(true);
    result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, times(0)).fire(1);
  }


}
