package com.tokopedia.checkout.revamp.view

import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutViewModelFieldTest : BaseCheckoutViewModelTest() {

    @Test
    fun checkout_page_source() {
        // given
        val source = "source"

        // when
        viewModel.checkoutPageSource = source

        // then
        assertEquals(source, viewModel.checkoutPageSource)
    }

    @Test
    fun trade_in() {
        // given
        val tradeIn = true

        // when
        viewModel.isTradeIn = tradeIn

        // then
        assertEquals(tradeIn, viewModel.isTradeIn)
    }

    @Test
    fun one_click_shipment() {
        // given
        val ocs = true

        // when
        viewModel.isOneClickShipment = ocs

        // then
        assertEquals(ocs, viewModel.isOneClickShipment)
    }

    @Test
    fun device_id() {
        // given
        val deviceId = "123321"

        // when
        viewModel.deviceId = deviceId

        // then
        assertEquals(deviceId, viewModel.deviceId)
    }

    @Test
    fun leasing_id() {
        // given
        val leasingId = "123321"

        // when
        viewModel.checkoutLeasingId = leasingId

        // then
        assertEquals(leasingId, viewModel.checkoutLeasingId)
    }
}
