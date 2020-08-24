package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.*
import com.tokopedia.utils.currency.CurrencyFormatUtil
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderSummaryPageViewModelCalculateTotalTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Calculate Total Invalid Quantity`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 0), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(), ButtonBayarState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Invalid Preference`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = false)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(), ButtonBayarState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Shop Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000), shop = OrderShop(errors = listOf("error")))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Quantity Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1, isStateError = true), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Shipment Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service", serviceErrorMessage = "error")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Shipment Invalid State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment()
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1000.0, 1000.0, 0.0), ButtonBayarState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Wholesale Price`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 10), productPrice = 1000, wholesalePrice = listOf(WholesalePrice(qtyMin = 10, prdPrc = 100))))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Invalid Wholesale Price`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000, wholesalePrice = listOf(WholesalePrice(qtyMin = 5, prdPrc = 100))))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Below Minimum`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, minimumAmount = 10000)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.NORMAL, true,
                "Belanjaanmu kurang dari min. transaksi ${orderSummaryPageViewModel._orderPayment.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderSummaryPageViewModel._orderPayment.minimumAmount, false)}). Silahkan pilih pembayaran lain."),
                orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Above Minimum`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, maximumAmount = 10)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.NORMAL, true,
                "Belanjaanmu melebihi limit transaksi ${orderSummaryPageViewModel._orderPayment.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderSummaryPageViewModel._orderPayment.maximumAmount, false)}). Silahkan pilih pembayaran lain."),
                orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Above OVO Balance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.NORMAL, true, OrderSummaryPageViewModel.OVO_INSUFFICIENT_ERROR_MESSAGE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Within OVO Balance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, walletAmount = 10000, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.NORMAL, false, null), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Has Payment Error Ticker`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorTickerMessage = "error"
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, errorTickerMessage = errorTickerMessage)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.NORMAL, true, errorTickerMessage), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Has Payment Error And Button`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, errorMessage = OrderPaymentErrorMessage(message = "error", button = OrderPaymentErrorMessageButton(text = "button")))

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), ButtonBayarState.DISABLE, false, null), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Promos`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
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
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 2000, isApplyLogisticPromo = true, logisticPromoViewModel = helper.logisticPromo,
                logisticPromoShipping = ShippingCourierUiModel().apply {
                    productData = ProductData().apply {
                        shipperProductId = 1
                    }
                },
                insuranceData = InsuranceData().apply {
                    insurancePrice = 100
                },
                isCheckInsurance = true, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1100.0, 1000.0, 2000.0, 100.0, 0.0, 1500, 500, listOf(
                "cashback" to "Rp1000"
        )), ButtonBayarState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Credit Card Mdr`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true,
                creditCard = OrderPaymentCreditCard(
                        numberOfCards = OrderPaymentCreditCardsNumber(1, 0, 1),
                        availableTerms = listOf(
                                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, isSelected = true),
                                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000),
                                OrderPaymentInstallmentTerm(term = 6, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 10000)
                        ),
                        selectedTerm = OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100)
                )
        )

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1523.0, 1000.0, 500.0, paymentFee = 23.0), ButtonBayarState.NORMAL, false, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(listOf(
                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, fee = 23.0, monthlyAmount = 1523.0, isEnable = true, isSelected = true),
                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000, fee = 23.0, monthlyAmount = 508.0, isEnable = true),
                OrderPaymentInstallmentTerm(term = 6, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 10000, fee = 23.0, monthlyAmount = 254.0, isEnable = false)
        ), orderSummaryPageViewModel.orderPayment.value.creditCard.availableTerms)
        assertEquals(OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, fee = 23.0, monthlyAmount = 1523.0, isEnable = true, isSelected = true), orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
    }

    @Test
    fun `Calculate Total Credit Card Mdr Subsidize`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000), shop = OrderShop(isOfficial = 1))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true,
                creditCard = OrderPaymentCreditCard(
                        numberOfCards = OrderPaymentCreditCardsNumber(1, 0, 1),
                        availableTerms = listOf(
                                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, isSelected = true),
                                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000),
                                OrderPaymentInstallmentTerm(term = 6, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 10000)
                        ),
                        selectedTerm = OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100)
                )
        )

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1515.0, 1000.0, 500.0, paymentFee = 15.0), ButtonBayarState.NORMAL, false, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(listOf(
                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, fee = 15.0, monthlyAmount = 1515.0, isEnable = true, isSelected = true),
                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000, fee = 15.0, monthlyAmount = 505.0, isEnable = true),
                OrderPaymentInstallmentTerm(term = 6, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 10000, fee = 15.0, monthlyAmount = 253.0, isEnable = false)
        ), orderSummaryPageViewModel.orderPayment.value.creditCard.availableTerms)
    }

    @Test
    fun `Calculate Total Has Error Installment`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true,
                creditCard = OrderPaymentCreditCard(
                        numberOfCards = OrderPaymentCreditCardsNumber(1, 0, 1),
                        availableTerms = listOf(
                                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100),
                                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isError = true)
                        ),
                        selectedTerm = OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isError = true)
                )
        )

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1523.0, 1000.0, 500.0, paymentFee = 23.0), ButtonBayarState.DISABLE, false, null), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Fix Error Installment`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true,
                creditCard = OrderPaymentCreditCard(
                        numberOfCards = OrderPaymentCreditCardsNumber(1, 0, 1),
                        availableTerms = listOf(
                                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100),
                                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000, isSelected = true, isError = true)
                        ),
                        selectedTerm = OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000, isSelected = true, isError = true)
                )
        )

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1523.0, 1000.0, 500.0, paymentFee = 23.0), ButtonBayarState.NORMAL, false, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000, fee = 23.0, monthlyAmount = 508.0, isSelected = true, isEnable = true), orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
    }
}