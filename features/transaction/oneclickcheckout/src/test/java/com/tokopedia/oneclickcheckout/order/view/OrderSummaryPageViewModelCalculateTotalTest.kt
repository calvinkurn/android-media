package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.*
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.utils.currency.CurrencyFormatUtil
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderSummaryPageViewModelCalculateTotalTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Calculate Total Invalid Quantity`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 0), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Invalid Preference`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = false)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Shop Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000), shop = OrderShop(errors = listOf("error")))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Quantity Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1, isStateError = true), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Shipment Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service", serviceErrorMessage = "error")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Shipment Invalid State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment()
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1000.0, 1000.0, 0.0), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Wholesale Price`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 10), productPrice = 1000, wholesalePrice = listOf(WholesalePrice(qtyMin = 10, prdPrc = 100))))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Invalid Wholesale Price`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000, wholesalePrice = listOf(WholesalePrice(qtyMin = 5, prdPrc = 100))))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Below Minimum`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, minimumAmount = 10000)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT,
                "Belanjaanmu kurang dari min. transaksi ${orderSummaryPageViewModel._orderPayment.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderSummaryPageViewModel._orderPayment.minimumAmount, false)}). Silahkan pilih pembayaran lain."),
                orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Revamp Below Minimum`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, minimumAmount = 10000)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentErrorData("Belanjaanmu kurang dari min. transaksi ${orderSummaryPageViewModel._orderPayment.gatewayName}.", "Ubah", OrderPaymentErrorData.ACTION_CHANGE_PAYMENT), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Below Minimum In OVO Only Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, minimumAmount = 10000, isOvoOnlyCampaign = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY,
                "Belanjaanmu kurang dari min. transaksi ${orderSummaryPageViewModel._orderPayment.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderSummaryPageViewModel._orderPayment.minimumAmount, false)})."),
                orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Above Maximum`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, maximumAmount = 10)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT,
                "Belanjaanmu melebihi limit transaksi ${orderSummaryPageViewModel._orderPayment.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderSummaryPageViewModel._orderPayment.maximumAmount, false)}). Silahkan pilih pembayaran lain."),
                orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Revamp Above Maximum`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, maximumAmount = 10)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentErrorData("Belanjaanmu melebihi limit transaksi ${orderSummaryPageViewModel._orderPayment.gatewayName}.", "Ubah", OrderPaymentErrorData.ACTION_CHANGE_PAYMENT), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Above Maximum In OVO Only Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, maximumAmount = 10, isOvoOnlyCampaign = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY,
                "Belanjaanmu melebihi limit transaksi ${orderSummaryPageViewModel._orderPayment.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(orderSummaryPageViewModel._orderPayment.maximumAmount, false)})."),
                orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total OVO No Phone Number`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorMessage = "error no phone"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(phoneNumber = OrderPaymentOvoActionData(isRequired = true, errorMessage = errorMessage))
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT, ""), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, message = errorMessage, type = OrderPaymentOvoErrorData.TYPE_MISSING_PHONE)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total OVO No Phone Number in OVO only campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorMessage = "error no phone"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(phoneNumber = OrderPaymentOvoActionData(isRequired = true, errorMessage = errorMessage))
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, isOvoOnlyCampaign = true, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, message = errorMessage, type = OrderPaymentOvoErrorData.TYPE_MISSING_PHONE)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total OVO Inactive`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorTicker = "error aktivasi"
        val callbackUrl = "url"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(activation = OrderPaymentOvoActionData(isRequired = true, buttonTitle = buttonTitle, errorTicker = errorTicker), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT, errorTicker), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, buttonTitle = buttonTitle, type = OrderPaymentOvoErrorData.TYPE_ACTIVATION, callbackUrl = callbackUrl)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total OVO Inactive in OVO only campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val callbackUrl = "url"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(activation = OrderPaymentOvoActionData(isRequired = true, buttonTitle = buttonTitle), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, isOvoOnlyCampaign = true, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, buttonTitle = buttonTitle, type = OrderPaymentOvoErrorData.TYPE_ACTIVATION, callbackUrl = callbackUrl)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total Above OVO Balance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorTicker = "error topup"
        val errorMessage = "error"
        val callbackUrl = "url"
        val isHideDigital = 1
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(topUp = OrderPaymentOvoActionData(buttonTitle = buttonTitle, errorTicker = errorTicker, errorMessage = errorMessage, isHideDigital = isHideDigital), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT, errorTicker), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, buttonTitle = buttonTitle, message = errorMessage, type = OrderPaymentOvoErrorData.TYPE_TOP_UP, callbackUrl = callbackUrl, isHideDigital = isHideDigital)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total Above OVO Balance In OVO Only Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorMessage = "error"
        val callbackUrl = "url"
        val isHideDigital = 1
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(topUp = OrderPaymentOvoActionData(buttonTitle = buttonTitle, errorMessage = errorMessage, isHideDigital = isHideDigital), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, isOvoOnlyCampaign = true, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, buttonTitle = buttonTitle, message = errorMessage, type = OrderPaymentOvoErrorData.TYPE_TOP_UP, callbackUrl = callbackUrl, isHideDigital = isHideDigital)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total Revamp Below Minimum In OVO Only Campaign`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, minimumAmount = 10000, isOvoOnlyCampaign = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Revamp Above Maximum In OVO Only Campaign`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, maximumAmount = 10, isOvoOnlyCampaign = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Revamp OVO No Phone Number`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorMessage = "error no phone"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(phoneNumber = OrderPaymentOvoActionData(isRequired = true, errorMessage = errorMessage))
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, message = errorMessage, type = OrderPaymentOvoErrorData.TYPE_MISSING_PHONE)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total Revamp OVO No Phone Number in OVO only campaign`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorMessage = "error no phone"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(phoneNumber = OrderPaymentOvoActionData(isRequired = true, errorMessage = errorMessage))
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, isOvoOnlyCampaign = true, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, message = errorMessage, type = OrderPaymentOvoErrorData.TYPE_MISSING_PHONE)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total Revamp OVO Inactive`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorTicker = "error aktivasi"
        val callbackUrl = "url"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(activation = OrderPaymentOvoActionData(isRequired = true, buttonTitle = buttonTitle, errorTicker = errorTicker), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, buttonTitle = buttonTitle, type = OrderPaymentOvoErrorData.TYPE_ACTIVATION, callbackUrl = callbackUrl)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total Revamp OVO Inactive in OVO only campaign`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val callbackUrl = "url"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(activation = OrderPaymentOvoActionData(isRequired = true, buttonTitle = buttonTitle), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, isOvoOnlyCampaign = true, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, buttonTitle = buttonTitle, type = OrderPaymentOvoErrorData.TYPE_ACTIVATION, callbackUrl = callbackUrl)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total Revamp Above OVO Balance`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorTicker = "error topup"
        val errorMessage = "error"
        val callbackUrl = "url"
        val isHideDigital = 1
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(topUp = OrderPaymentOvoActionData(buttonTitle = buttonTitle, errorTicker = errorTicker, errorMessage = errorMessage, isHideDigital = isHideDigital), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, buttonTitle = buttonTitle, message = errorMessage, type = OrderPaymentOvoErrorData.TYPE_TOP_UP, callbackUrl = callbackUrl, isHideDigital = isHideDigital)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total Revamp Above OVO Balance In OVO Only Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorMessage = "error"
        val callbackUrl = "url"
        val isHideDigital = 1
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(topUp = OrderPaymentOvoActionData(buttonTitle = buttonTitle, errorMessage = errorMessage, isHideDigital = isHideDigital), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, isOvoOnlyCampaign = true, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel._orderPayment = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(orderPayment.copy(
                isCalculationError = true,
                ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, buttonTitle = buttonTitle, message = errorMessage, type = OrderPaymentOvoErrorData.TYPE_TOP_UP, callbackUrl = callbackUrl, isHideDigital = isHideDigital)
        ), orderSummaryPageViewModel.orderPayment.value)
    }

    @Test
    fun `Calculate Total Within OVO Balance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, walletAmount = 10000, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Has Payment Error Ticker`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorTickerMessage = "error"
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, errorTickerMessage = errorTickerMessage)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT, errorTickerMessage), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Has Payment Error Ticker With Disabled Button Pay`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorTickerMessage = "error"
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, errorTickerMessage = errorTickerMessage, isDisablePayButton = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, errorTickerMessage), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Has Payment Error Ticker With Enable Button Next`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorTickerMessage = "error"
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, errorTickerMessage = errorTickerMessage, isEnableNextButton = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CONTINUE, errorTickerMessage), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Has Payment Error And Button`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, errorMessage = OrderPaymentErrorMessage(message = "error", button = OrderPaymentErrorMessageButton(text = "button")))

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Revamp Has Payment Error And Button`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val message = "error"
        val button = "button"
        val action = "action"
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, revampErrorMessage = OrderPaymentRevampErrorMessage(message = message, button = OrderPaymentRevampErrorMessageButton(text = button, action = action)))

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.NORMAL, OccButtonType.CHOOSE_PAYMENT, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentErrorData(message, button, action), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Has Payment Error And Button Also Disable Pay Button`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val message = "error"
        val button = "button"
        val action = "action"
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, revampErrorMessage = OrderPaymentRevampErrorMessage(message = message, button = OrderPaymentRevampErrorMessageButton(text = button, action = action)), isDisablePayButton = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentErrorData(message, button, action), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Has Payment Credit Card Error And Button`() {
        // Given
        orderSummaryPageViewModel.revampData = OccRevampData(true)
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val message = "error"
        val button = "button"
        val action = "action"
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, revampErrorMessage = OrderPaymentRevampErrorMessage(message = message, button = OrderPaymentRevampErrorMessageButton(text = button, action = action)), errorMessage = OrderPaymentErrorMessage(message = "cc error", button = OrderPaymentErrorMessageButton(text = "cc button")))

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentErrorData(message, button, action), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total With Promos`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(promoUiModel = PromoUiModel(
                additionalInfoUiModel = AdditionalInfoUiModel(
                        usageSummariesUiModel = arrayListOf(
                                UsageSummariesUiModel(
                                        desc = "Dapat Cashback Senilai",
                                        type = "cashback",
                                        amountStr = "Rp1.000.000",
                                        amount = 1000000,
                                        currencyDetailStr = "(20.000 TokoPoints)"
                                )
                        )
                ),
                benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(
                        summaries = listOf(SummariesItemUiModel(type = SummariesUiModel.TYPE_DISCOUNT, details = listOf(
                                DetailsItemUiModel(type = SummariesUiModel.TYPE_SHIPPING_DISCOUNT, amount = helper.logisticPromo.benefitAmount),
                                DetailsItemUiModel(type = SummariesUiModel.TYPE_PRODUCT_DISCOUNT, amount = 500)
                        )), SummariesItemUiModel(type = SummariesUiModel.TYPE_CASHBACK, details = listOf(
                                DetailsItemUiModel(description = "cashback", amountStr = "Rp1000")
                        )))
                )))
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
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
        assertEquals(
                OrderTotal(
                        OrderCost(
                                1100.0,
                                1000.0,
                                2000.0,
                                100.0,
                                0.0,
                                1500,
                                500,
                                0,
                                listOf(
                                        OrderCostCashbackData(
                                                description = "Dapat Cashback Senilai",
                                                amountStr = "Rp1.000.000",
                                                currencyDetailStr = "(20.000 TokoPoints)"
                                        )
                                )
                        ),
                        OccButtonState.NORMAL
                ),
                orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Credit Card Mdr`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
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
        assertEquals(OrderTotal(OrderCost(1523.0, 1000.0, 500.0, paymentFee = 23.0), OccButtonState.NORMAL, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
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
        assertEquals(OrderTotal(OrderCost(1515.0, 1000.0, 500.0, paymentFee = 15.0), OccButtonState.NORMAL, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(listOf(
                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, fee = 15.0, monthlyAmount = 1515.0, isEnable = true, isSelected = true),
                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000, fee = 15.0, monthlyAmount = 505.0, isEnable = true),
                OrderPaymentInstallmentTerm(term = 6, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 10000, fee = 15.0, monthlyAmount = 253.0, isEnable = false)
        ), orderSummaryPageViewModel.orderPayment.value.creditCard.availableTerms)
    }

    @Test
    fun `Calculate Total Has Error Installment`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
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
        assertEquals(OrderTotal(OrderCost(1523.0, 1000.0, 500.0, paymentFee = 23.0), OccButtonState.DISABLE, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Fix Error Installment`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
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
        assertEquals(OrderTotal(OrderCost(1523.0, 1000.0, 500.0, paymentFee = 23.0), OccButtonState.NORMAL, OccButtonType.PAY, null), orderSummaryPageViewModel.orderTotal.value)
        assertEquals(OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000, fee = 23.0, monthlyAmount = 508.0, isSelected = true, isEnable = true), orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
    }

    @Test
    fun `Calculate Total with Purchase Protection Checked`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000, purchaseProtectionPlanData = PurchaseProtectionPlanData(protectionPricePerProduct = 1000, stateChecked = PurchaseProtectionPlanData.STATE_TICKED)))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(2500.0, 1000.0, 500.0, purchaseProtectionPrice = 1000), OccButtonState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total with Purchase Protection Checked and Multiple Quantity `() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 2), productPrice = 1000, purchaseProtectionPlanData = PurchaseProtectionPlanData(protectionPricePerProduct = 1000, stateChecked = PurchaseProtectionPlanData.STATE_TICKED)))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(4500.0, 2000.0, 500.0, purchaseProtectionPrice = 2000), OccButtonState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total with Purchase Protection Unchecked from State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000, purchaseProtectionPlanData = PurchaseProtectionPlanData(protectionPricePerProduct = 1000, stateChecked = PurchaseProtectionPlanData.STATE_UNTICKED)))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0, purchaseProtectionPrice = 0), OccButtonState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total with Purchase Protection Unchecked`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(product = OrderProduct(quantity = QuantityUiModel(orderQuantity = 1), productPrice = 1000, purchaseProtectionPlanData = PurchaseProtectionPlanData(protectionPricePerProduct = 1000, stateChecked = PurchaseProtectionPlanData.STATE_EMPTY)))
        orderSummaryPageViewModel._orderPreference = OrderPreference(isValid = true)
        orderSummaryPageViewModel._orderShipment = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(1500.0, 1000.0, 500.0, purchaseProtectionPrice = 0), OccButtonState.NORMAL), orderSummaryPageViewModel.orderTotal.value)
    }

}