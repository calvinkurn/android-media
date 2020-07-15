package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.oneclickcheckout.common.data.model.Payment
import com.tokopedia.oneclickcheckout.order.data.get.ProfileResponse
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.*
import com.tokopedia.utils.currency.CurrencyFormatUtil
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderSummaryPageViewModelCalculateTotalTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Calculate Total`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(shipping = OrderShipment(shippingPrice = 500),
                preference = ProfileResponse(payment = Payment()))

        orderSummaryPageViewModel.calculateTotal()

        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Below Minimum`() {
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(shipping = OrderShipment(shippingPrice = 500),
                preference = ProfileResponse(payment = Payment(minimumAmount = 10000)))

        orderSummaryPageViewModel.calculateTotal()

        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.DISABLE, true,
                "Belanjaanmu kurang dari min. transaksi ${orderSummaryPageViewModel._orderPreference?.preference?.payment?.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderSummaryPageViewModel._orderPreference?.preference?.payment?.minimumAmount ?: 0, false)}). Silahkan pilih pembayaran lain."),
                orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Above Minimum`() {
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(shipping = OrderShipment(shippingPrice = 500),
                preference = ProfileResponse(payment = Payment(maximumAmount = 10)))

        orderSummaryPageViewModel.calculateTotal()

        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.DISABLE, true,
                "Belanjaanmu melebihi limit transaksi ${orderSummaryPageViewModel._orderPreference?.preference?.payment?.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderSummaryPageViewModel._orderPreference?.preference?.payment?.maximumAmount ?: 0, false)}). Silahkan pilih pembayaran lain."),
                orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Above OVO Balance`() {
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(shipping = OrderShipment(shippingPrice = 500),
                preference = ProfileResponse(payment = Payment(walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE)))

        orderSummaryPageViewModel.calculateTotal()

        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.DISABLE, true, OrderSummaryPageViewModel.OVO_INSUFFICIENT_ERROR_MESSAGE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Promos`() {
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(
                summaries = listOf(SummariesItemUiModel(type = SummariesUiModel.TYPE_DISCOUNT, details = listOf(
                        DetailsItemUiModel(type = SummariesUiModel.TYPE_SHIPPING_DISCOUNT, amount = helper.logisticPromo.benefitAmount),
                        DetailsItemUiModel(type = SummariesUiModel.TYPE_PRODUCT_DISCOUNT, amount = 500)
                )), SummariesItemUiModel(type = SummariesUiModel.TYPE_CASHBACK, details = listOf(
                        DetailsItemUiModel(description = "cashback", amountStr = "Rp1000")
                )))
        )))
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(shipping = OrderShipment(shippingPrice = 2000, isApplyLogisticPromo = true, logisticPromoViewModel = helper.logisticPromo, logisticPromoShipping = ShippingCourierUiModel()
                , insuranceData = InsuranceData().apply {
            insurancePrice = 100
        }, isCheckInsurance = true), preference = ProfileResponse(payment = Payment()))

        orderSummaryPageViewModel.calculateTotal()

        assertEquals(OrderTotal(OrderCost(1100.0, 1000.0, 2000.0, 100.0, 0.0, 1500, 500, listOf(
                "cashback" to "Rp1000"
        )), ButtonBayarState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }
}