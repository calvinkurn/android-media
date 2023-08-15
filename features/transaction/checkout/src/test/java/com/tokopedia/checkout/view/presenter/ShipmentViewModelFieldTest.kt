package com.tokopedia.checkout.view.presenter

import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentViewModelFieldTest : BaseShipmentViewModelTest() {

    @Test
    fun `WHEN set checkout leasing id THEN should set checkout leasing id`() {
        // Given
        val checkoutLeasingId = "123321"

        // When
        viewModel.checkoutLeasingId = checkoutLeasingId

        // Then
        assertEquals(checkoutLeasingId, viewModel.checkoutLeasingId)
    }

    @Test
    fun `WHEN set device id THEN should set device id`() {
        // Given
        val deviceId = "123321"

        // When
        viewModel.deviceId = deviceId

        // Then
        assertEquals(deviceId, viewModel.deviceId)
    }

    @Test
    fun `WHEN set ocs THEN should set ocs`() {
        // Given
        val isOneClickShipment = true

        // When
        viewModel.isOneClickShipment = isOneClickShipment

        // Then
        assertEquals(isOneClickShipment, viewModel.isOneClickShipment)
    }

    @Test
    fun `WHEN set tradein THEN should set tradein`() {
        // Given
        val isTradeIn = true

        // When
        viewModel.isTradeIn = isTradeIn

        // Then
        assertEquals(isTradeIn, viewModel.isTradeIn)
    }
}
