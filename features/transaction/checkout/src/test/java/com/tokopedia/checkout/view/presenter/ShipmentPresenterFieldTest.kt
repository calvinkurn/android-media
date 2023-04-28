package com.tokopedia.checkout.view.presenter

import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentPresenterFieldTest : BaseShipmentPresenterTest() {

    @Test
    fun `WHEN set checkout leasing id THEN should set checkout leasing id`() {
        // Given
        val checkoutLeasingId = "123321"

        // When
        presenter.checkoutLeasingId = checkoutLeasingId

        // Then
        assertEquals(checkoutLeasingId, presenter.checkoutLeasingId)
    }

    @Test
    fun `WHEN set device id THEN should set device id`() {
        // Given
        val deviceId = "123321"

        // When
        presenter.deviceId = deviceId

        // Then
        assertEquals(deviceId, presenter.deviceId)
    }

    @Test
    fun `WHEN set ocs THEN should set ocs`() {
        // Given
        val isOneClickShipment = true

        // When
        presenter.isOneClickShipment = isOneClickShipment

        // Then
        assertEquals(isOneClickShipment, presenter.isOneClickShipment)
    }

    @Test
    fun `WHEN set tradein THEN should set tradein`() {
        // Given
        val isTradeIn = true

        // When
        presenter.isTradeIn = isTradeIn

        // Then
        assertEquals(isTradeIn, presenter.isTradeIn)
    }
}
