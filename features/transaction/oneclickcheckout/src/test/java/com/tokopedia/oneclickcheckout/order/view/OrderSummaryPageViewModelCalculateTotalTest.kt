package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.BenefitSummaryInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.DetailsItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.UsageSummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderSummaryPageViewModelCalculateTotalTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Calculate Total Invalid Quantity`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 0, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total Invalid Preference`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(address = OrderProfileAddress())

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Shop Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)), shop = OrderShop(errors = listOf("error")))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Quantity Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 0, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Shipment Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service", serviceErrorMessage = "error")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total With Shipment Invalid State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment()
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(OrderTotal(OrderCost(), OccButtonState.DISABLE), orderSummaryPageViewModel.orderTotal.value)
    }

    @Test
    fun `Calculate Total`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total With Wholesale Price`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 10, productPrice = 1000.0, wholesalePriceList = listOf(WholesalePrice(qtyMin = 10, prdPrc = 100.0)))))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Multiple Products Same Parent With Wholesale Price`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(
            products = mutableListOf(
                OrderProduct(productId = "1", parentId = "1", orderQuantity = 5, productPrice = 1000.0, wholesalePriceList = listOf(WholesalePrice(qtyMin = 10, prdPrc = 100.0))),
                OrderProduct(productId = "2", parentId = "1", orderQuantity = 5, productPrice = 1000.0, wholesalePriceList = listOf(WholesalePrice(qtyMin = 10, prdPrc = 100.0)))
            )
        )
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Multiple Products Different Parent With Wholesale Price`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(
            products = mutableListOf(
                OrderProduct(productId = "1", parentId = "1", orderQuantity = 5, productPrice = 1000.0, wholesalePriceList = listOf(WholesalePrice(qtyMin = 10, prdPrc = 100.0))),
                OrderProduct(productId = "2", parentId = "2", orderQuantity = 5, productPrice = 1000.0, wholesalePriceList = listOf(WholesalePrice(qtyMin = 10, prdPrc = 100.0)))
            )
        )
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    10500.0,
                    10000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 10500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 10500.0,
                    totalPriceWithoutPaymentFees = 10500.0
                ),
                OccButtonState.NORMAL
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total With Invalid Wholesale Price`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0, wholesalePriceList = listOf(WholesalePrice(qtyMin = 5, prdPrc = 100.0)))))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Revamp Below Minimum`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, minimumAmount = 10000)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData("Belanjaanmu kurang dari min. transaksi ${orderSummaryPageViewModel.orderPayment.value.gatewayName}.", "Ubah", OrderPaymentErrorData.ACTION_CHANGE_PAYMENT), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Above Maximum`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 10)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData("Belanjaanmu melebihi limit transaksi ${orderSummaryPageViewModel.orderPayment.value.gatewayName}.", "Ubah", OrderPaymentErrorData.ACTION_CHANGE_PAYMENT), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Below Minimum In OVO Only Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, minimumAmount = 10000, isOvoOnlyCampaign = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Revamp Below Minimum In Specific Gateway Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, minimumAmount = 10000, specificGatewayCampaignOnlyType = 2)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Revamp Below Minimum In GoCicil`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorBelowMinimum = "errorBelowMinimum"
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, minimumAmount = 10000, walletData = OrderPaymentWalletAdditionalData(walletType = 4, goCicilData = OrderPaymentGoCicilData(errorMessageBottomLimit = errorBelowMinimum)))

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData(errorBelowMinimum), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Belom Minimum In GoCicil Specific Gateway Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorBelowMinimum = "errorBelowMinimum"
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, minimumAmount = 10000, specificGatewayCampaignOnlyType = 4, walletData = OrderPaymentWalletAdditionalData(walletType = 4, goCicilData = OrderPaymentGoCicilData(errorMessageBottomLimit = errorBelowMinimum)))

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData(errorBelowMinimum), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Above Maximum In OVO Only Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 10, isOvoOnlyCampaign = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Revamp Above Maximum In Specific Gateway Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 10, specificGatewayCampaignOnlyType = 2)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Revamp Above Maximum In GoCicil`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorAboveMaximum = "errorAboveMaximum"
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 10, walletData = OrderPaymentWalletAdditionalData(walletType = 4, goCicilData = OrderPaymentGoCicilData(errorMessageTopLimit = errorAboveMaximum)))

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData("Belanjaanmu melebihi limit transaksi ${orderSummaryPageViewModel.orderPayment.value.gatewayName}.", "Ubah", OrderPaymentErrorData.ACTION_CHANGE_PAYMENT), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Above Maximum In GoCicil Specific Gateway Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorAboveMaximum = "errorAboveMaximum"
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 10, specificGatewayCampaignOnlyType = 4, walletData = OrderPaymentWalletAdditionalData(walletType = 4, goCicilData = OrderPaymentGoCicilData(errorMessageTopLimit = errorAboveMaximum)))

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData("Belanjaanmu melebihi limit transaksi ${orderSummaryPageViewModel.orderPayment.value.gatewayName}.", "Ubah", OrderPaymentErrorData.ACTION_CHANGE_PAYMENT), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp OVO No Phone Number`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorMessage = "error no phone"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(phoneNumber = OrderPaymentOvoActionData(isRequired = true, errorMessage = errorMessage))
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = errorMessage, type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE, isOvo = true)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp Wallet No Phone Number`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorMessage = "error no phone"
        val walletAdditionalData = OrderPaymentWalletAdditionalData(enableWalletAmountValidation = true, phoneNumber = OrderPaymentWalletActionData(isRequired = true, errorMessage = errorMessage))
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, walletData = walletAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = errorMessage, type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp OVO No Phone Number in OVO only campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorMessage = "error no phone"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(phoneNumber = OrderPaymentOvoActionData(isRequired = true, errorMessage = errorMessage))
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, isOvoOnlyCampaign = true, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = errorMessage, type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE, isOvo = true)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp Wallet No Phone Number in Specific Gateway Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorMessage = "error no phone"
        val walletAdditionalData = OrderPaymentWalletAdditionalData(enableWalletAmountValidation = true, phoneNumber = OrderPaymentWalletActionData(isRequired = true, errorMessage = errorMessage))
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, specificGatewayCampaignOnlyType = 2, walletData = walletAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = errorMessage, type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp OVO Inactive`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorTicker = "error aktivasi"
        val callbackUrl = "url"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(activation = OrderPaymentOvoActionData(isRequired = true, buttonTitle = buttonTitle, errorTicker = errorTicker), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, buttonTitle = buttonTitle, type = OrderPaymentWalletErrorData.TYPE_ACTIVATION, callbackUrl = callbackUrl, isOvo = true)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp Wallet Inactive`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val callbackUrl = "url"
        val walletAdditionalData = OrderPaymentWalletAdditionalData(enableWalletAmountValidation = true, activation = OrderPaymentWalletActionData(isRequired = true, buttonTitle = buttonTitle), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, walletData = walletAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, buttonTitle = buttonTitle, type = OrderPaymentWalletErrorData.TYPE_ACTIVATION, callbackUrl = callbackUrl)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp OVO Inactive in OVO only campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val callbackUrl = "url"
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(activation = OrderPaymentOvoActionData(isRequired = true, buttonTitle = buttonTitle), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, isOvoOnlyCampaign = true, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, buttonTitle = buttonTitle, type = OrderPaymentWalletErrorData.TYPE_ACTIVATION, callbackUrl = callbackUrl, isOvo = true)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp Wallet Inactive in Specific Gateway campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val callbackUrl = "url"
        val walletAdditionalData = OrderPaymentWalletAdditionalData(enableWalletAmountValidation = true, activation = OrderPaymentWalletActionData(isRequired = true, buttonTitle = buttonTitle), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, specificGatewayCampaignOnlyType = 2, walletData = walletAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, buttonTitle = buttonTitle, type = OrderPaymentWalletErrorData.TYPE_ACTIVATION, callbackUrl = callbackUrl)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp Above OVO Balance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorTicker = "error topup"
        val errorMessage = "error"
        val callbackUrl = "url"
        val isHideDigital = 1
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(topUp = OrderPaymentOvoActionData(buttonTitle = buttonTitle, errorTicker = errorTicker, errorMessage = errorMessage, isHideDigital = isHideDigital), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = true, buttonTitle = buttonTitle, message = errorMessage, type = OrderPaymentWalletErrorData.TYPE_TOP_UP, callbackUrl = callbackUrl, isHideDigital = isHideDigital, isOvo = true)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp Above Wallet Balance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorMessage = "error"
        val callbackUrl = "url"
        val isHideDigital = true
        val walletAdditionalData = OrderPaymentWalletAdditionalData(enableWalletAmountValidation = true, topUp = OrderPaymentWalletActionData(buttonTitle = buttonTitle, errorMessage = errorMessage, isHideDigital = isHideDigital), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, walletData = walletAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = true, buttonTitle = buttonTitle, message = errorMessage, type = OrderPaymentWalletErrorData.TYPE_TOP_UP, callbackUrl = callbackUrl, isHideDigital = 1)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp GoCicil Above Wallet Balance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorAboveLimit = "errorAboveLimit"
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 100000, walletAmount = 10, walletData = OrderPaymentWalletAdditionalData(walletType = 4, goCicilData = OrderPaymentGoCicilData(errorMessageTopLimit = errorAboveLimit)))

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData(errorAboveLimit), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Above OVO Balance In OVO Only Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorMessage = "error"
        val callbackUrl = "url"
        val isHideDigital = 1
        val orderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(topUp = OrderPaymentOvoActionData(buttonTitle = buttonTitle, errorMessage = errorMessage, isHideDigital = isHideDigital), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE, isOvoOnlyCampaign = true, ovoData = orderPaymentOvoAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = true, buttonTitle = buttonTitle, message = errorMessage, type = OrderPaymentWalletErrorData.TYPE_TOP_UP, callbackUrl = callbackUrl, isHideDigital = isHideDigital, isOvo = true)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp Above Wallet Balance In Specific Gateway Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val buttonTitle = "button"
        val errorMessage = "error"
        val callbackUrl = "url"
        val isHideDigital = false
        val walletAdditionalData = OrderPaymentWalletAdditionalData(enableWalletAmountValidation = true, topUp = OrderPaymentWalletActionData(buttonTitle = buttonTitle, errorMessage = errorMessage, isHideDigital = isHideDigital), callbackUrl = callbackUrl)
        val orderPayment = OrderPayment(isEnable = true, walletAmount = 10, specificGatewayCampaignOnlyType = 2, walletData = walletAdditionalData)
        orderSummaryPageViewModel.orderPayment.value = orderPayment

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            orderPayment.copy(
                isCalculationError = true,
                walletErrorData = OrderPaymentWalletErrorData(isBlockingError = true, buttonTitle = buttonTitle, message = errorMessage, type = OrderPaymentWalletErrorData.TYPE_TOP_UP, callbackUrl = callbackUrl, isHideDigital = 0)
            ),
            orderSummaryPageViewModel.orderPayment.value
        )
    }

    @Test
    fun `Calculate Total Revamp GoCicil Above Wallet Balance In Specific Gateway Campaign`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorAboveLimit = "errorAboveLimit"
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 100000, walletAmount = 10, specificGatewayCampaignOnlyType = 4, walletData = OrderPaymentWalletAdditionalData(walletType = 4, goCicilData = OrderPaymentGoCicilData(errorMessageTopLimit = errorAboveLimit)))

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData(errorAboveLimit), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Within OVO Balance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, walletAmount = 10000, gatewayCode = OrderSummaryPageViewModel.OVO_GATEWAY_CODE)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Within Wallet Balance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, walletAmount = 10000, walletData = OrderPaymentWalletAdditionalData(enableWalletAmountValidation = true))

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Revamp GoCicil Within Wallet Balance And Valid Term`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true, maximumAmount = 100000, walletAmount = 100000,
            walletData = OrderPaymentWalletAdditionalData(
                walletType = 4,
                goCicilData = OrderPaymentGoCicilData(availableTerms = listOf(OrderPaymentGoCicilTerms(installmentTerm = 2, isActive = true)), selectedTerm = OrderPaymentGoCicilTerms(installmentTerm = 2, isActive = true))
            )
        )

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    installmentData = OrderCostInstallmentData(installmentTerm = 2),
                    isInstallment = true
                ),
                OccButtonState.NORMAL,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(null, orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Has Payment Error And Button`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val message = "error"
        val button = "button"
        val action = "action"
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, revampErrorMessage = OrderPaymentRevampErrorMessage(message = message, button = OrderPaymentRevampErrorMessageButton(text = button, action = action)))

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData(message, button, action), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Has Payment Error And Button Also Disable Pay Button`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val message = "error"
        val button = "button"
        val action = "action"
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, revampErrorMessage = OrderPaymentRevampErrorMessage(message = message, button = OrderPaymentRevampErrorMessageButton(text = button, action = action)), isDisablePayButton = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData(message, button, action), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total Revamp Has Payment Credit Card Error And Button`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val message = "error"
        val button = "button"
        val action = "action"
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, revampErrorMessage = OrderPaymentRevampErrorMessage(message = message, button = OrderPaymentRevampErrorMessageButton(text = button, action = action)), errorMessage = OrderPaymentErrorMessage(message = "cc error", button = OrderPaymentErrorMessageButton(text = "cc button")))

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData(message, button, action), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total GoCicil With No Active Term`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        val errorUnavailableTenures = "errorUnavailableTenures"
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true, maximumAmount = 100000, walletAmount = 100000,
            walletData = OrderPaymentWalletAdditionalData(
                walletType = 4,
                goCicilData = OrderPaymentGoCicilData(availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = false)), errorMessageUnavailableTenures = errorUnavailableTenures)
            )
        )

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.NORMAL,
                OccButtonType.CHOOSE_PAYMENT
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentErrorData(errorUnavailableTenures), orderSummaryPageViewModel.orderPayment.value.errorData)
    }

    @Test
    fun `Calculate Total With Promos`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
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
                    summaries = listOf(
                        SummariesItemUiModel(
                            type = SummariesUiModel.TYPE_DISCOUNT,
                            details = listOf(
                                DetailsItemUiModel(type = SummariesUiModel.TYPE_SHIPPING_DISCOUNT, amount = helper.logisticPromo.benefitAmount),
                                DetailsItemUiModel(type = SummariesUiModel.TYPE_PRODUCT_DISCOUNT, amount = 500)
                            )
                        ),
                        SummariesItemUiModel(
                            type = SummariesUiModel.TYPE_CASHBACK,
                            details = listOf(
                                DetailsItemUiModel(description = "cashback", amountStr = "Rp1000")
                            )
                        )
                    )
                )
            )
        )
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(
            shippingPrice = 2000,
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = ShippingCourierUiModel().apply {
                productData = ProductData().apply {
                    shipperProductId = 1
                }
            },
            insurance = OrderInsurance(
                InsuranceData().apply {
                    insurancePrice = 100.0
                },
                isCheckInsurance = true
            ),
            serviceName = "service"
        )
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

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
                    true,
                    0.0,
                    1500,
                    500,
                    0,
                    0.0,
                    false,
                    listOf(
                        OrderCostCashbackData(
                            description = "Dapat Cashback Senilai",
                            amountStr = "Rp1.000.000",
                            currencyDetailStr = "(20.000 TokoPoints)"
                        )
                    ),
                    totalItemPriceAndShippingFee = 3000.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 3100.0,
                    totalPriceWithoutPaymentFees = 1100.0,
                    totalAdditionalFee = 100.0,
                    totalDiscounts = 2000
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
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true,
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
        assertEquals(
            OrderTotal(
                OrderCost(
                    1523.0,
                    1000.0,
                    500.0,
                    paymentFee = 23.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.NORMAL,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            listOf(
                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, fee = 23.0, monthlyAmount = 1523.0, isEnable = true, isSelected = true),
                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000, fee = 23.0, monthlyAmount = 508.0, isEnable = true),
                OrderPaymentInstallmentTerm(term = 6, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 10000, fee = 23.0, monthlyAmount = 254.0, isEnable = false)
            ),
            orderSummaryPageViewModel.orderPayment.value.creditCard.availableTerms
        )
        assertEquals(OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, fee = 23.0, monthlyAmount = 1523.0, isEnable = true, isSelected = true), orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
    }

    @Test
    fun `Calculate Total Credit Card Mdr Subsidize`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)), shop = OrderShop(isOfficial = 1))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true,
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
        assertEquals(
            OrderTotal(
                OrderCost(
                    1515.0,
                    1000.0,
                    500.0,
                    paymentFee = 15.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.NORMAL,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(
            listOf(
                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, fee = 15.0, monthlyAmount = 1515.0, isEnable = true, isSelected = true),
                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000, fee = 15.0, monthlyAmount = 505.0, isEnable = true),
                OrderPaymentInstallmentTerm(term = 6, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 10000, fee = 15.0, monthlyAmount = 253.0, isEnable = false)
            ),
            orderSummaryPageViewModel.orderPayment.value.creditCard.availableTerms
        )
    }

    @Test
    fun `Calculate Total Has Error Installment`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true,
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
        assertEquals(
            OrderTotal(
                OrderCost(
                    1523.0,
                    1000.0,
                    500.0,
                    paymentFee = 23.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Fix Error Installment`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true,
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
        assertEquals(
            OrderTotal(
                OrderCost(
                    1523.0,
                    1000.0,
                    500.0,
                    paymentFee = 23.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.NORMAL,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
        assertEquals(OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 1000, fee = 23.0, monthlyAmount = 508.0, isSelected = true, isEnable = true), orderSummaryPageViewModel.orderPayment.value.creditCard.selectedTerm)
    }

    @Test
    fun `Calculate Total with Purchase Protection Checked`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0, purchaseProtectionPlanData = PurchaseProtectionPlanData(protectionPricePerProduct = 1000, stateChecked = PurchaseProtectionPlanData.STATE_TICKED))))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    2500.0,
                    1000.0,
                    500.0,
                    purchaseProtectionPrice = 1000,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 2500.0,
                    totalPriceWithoutPaymentFees = 2500.0,
                    totalAdditionalFee = 1000.0
                ),
                OccButtonState.NORMAL
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total with Purchase Protection Checked and Multiple Quantity `() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 2, productPrice = 1000.0, purchaseProtectionPlanData = PurchaseProtectionPlanData(protectionPricePerProduct = 1000, stateChecked = PurchaseProtectionPlanData.STATE_TICKED))))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    4500.0,
                    2000.0,
                    500.0,
                    purchaseProtectionPrice = 2000,
                    totalItemPriceAndShippingFee = 2500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 4500.0,
                    totalPriceWithoutPaymentFees = 4500.0,
                    totalAdditionalFee = 2000.0
                ),
                OccButtonState.NORMAL
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total with Purchase Protection Unchecked from State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0, purchaseProtectionPlanData = PurchaseProtectionPlanData(protectionPricePerProduct = 1000, stateChecked = PurchaseProtectionPlanData.STATE_UNTICKED))))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    purchaseProtectionPrice = 0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total with Purchase Protection Unchecked`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0, purchaseProtectionPlanData = PurchaseProtectionPlanData(protectionPricePerProduct = 1000, stateChecked = PurchaseProtectionPlanData.STATE_EMPTY))))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    purchaseProtectionPrice = 0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0
                ),
                OccButtonState.NORMAL
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total GoCicil With Selected Inactive Term`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true, maximumAmount = 100000, walletAmount = 100000,
            walletData = OrderPaymentWalletAdditionalData(
                walletType = 4,
                goCicilData = OrderPaymentGoCicilData(availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = false), OrderPaymentGoCicilTerms(isActive = true)), selectedTerm = OrderPaymentGoCicilTerms(isActive = false))
            )
        )

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total GoCicil With No Selected Term`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true, maximumAmount = 100000, walletAmount = 100000,
            walletData = OrderPaymentWalletAdditionalData(
                walletType = 4,
                goCicilData = OrderPaymentGoCicilData(availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = false), OrderPaymentGoCicilTerms(isActive = true)), selectedTerm = null)
            )
        )

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total GoCicil With No Terms`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true, maximumAmount = 100000, walletAmount = 100000,
            walletData = OrderPaymentWalletAdditionalData(
                walletType = 4,
                goCicilData = OrderPaymentGoCicilData(availableTerms = emptyList(), selectedTerm = OrderPaymentGoCicilTerms(isActive = false))
            )
        )

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    1500.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Non Installment With Payment Fee Details`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 100000, walletAmount = 100000, originalPaymentFees = helper.paymentFeeDetails)

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    3000.0,
                    1000.0,
                    500.0,
                    paymentFee = 1500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    orderPaymentFees = helper.paymentFeeDetails
                ),
                OccButtonState.NORMAL,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Gocicil With Payment Fee Details`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true,
            maximumAmount = 100000,
            walletAmount = 100000,
            walletData = OrderPaymentWalletAdditionalData(
                walletType = 4,
                goCicilData = OrderPaymentGoCicilData(availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = false), OrderPaymentGoCicilTerms(isActive = true)), selectedTerm = null)
            ),
            originalPaymentFees = helper.paymentFeeDetails
        )

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    3000.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true,
                    orderPaymentFees = helper.paymentFeeDetails
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Credit Card With Payment Fee Details`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true,
            creditCard = OrderPaymentCreditCard(
                numberOfCards = OrderPaymentCreditCardsNumber(1, 0, 1),
                availableTerms = listOf(
                    OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100),
                    OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isError = true)
                ),
                selectedTerm = OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isError = true)
            ),
            originalPaymentFees = helper.paymentFeeDetails
        )

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    3045.0, 1000.0, 500.0, paymentFee = 45.0,
                    totalItemPriceAndShippingFee = 1500.0, totalPriceWithoutDiscountsAndPaymentFees = 1500.0, totalPriceWithoutPaymentFees = 1500.0, isInstallment = true, orderPaymentFees = helper.paymentFeeDetails
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Non Installment With Dynamic Payment Fee Details`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, maximumAmount = 100000, walletAmount = 100000, originalPaymentFees = helper.paymentFeeDetails)

        val orderPaymentFee = OrderPaymentFee(
            title = "dynamic payment fee",
            fee = 5000.0
        )
        coEvery { dynamicPaymentFeeUseCase.invoke(any()) } returns listOf(orderPaymentFee)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    8000.0,
                    1000.0,
                    500.0,
                    paymentFee = 6500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    orderPaymentFees = helper.paymentFeeDetails + orderPaymentFee
                ),
                OccButtonState.NORMAL,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Gocicil With Dynamic Payment Fee Details`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true,
            maximumAmount = 100000,
            walletAmount = 100000,
            walletData = OrderPaymentWalletAdditionalData(
                walletType = 4,
                goCicilData = OrderPaymentGoCicilData(availableTerms = listOf(OrderPaymentGoCicilTerms(isActive = false), OrderPaymentGoCicilTerms(isActive = true)), selectedTerm = null)
            ),
            originalPaymentFees = helper.paymentFeeDetails
        )
        val orderPaymentFee = OrderPaymentFee(
            title = "dynamic payment fee",
            fee = 5000.0
        )
        coEvery { dynamicPaymentFeeUseCase.invoke(any()) } returns listOf(orderPaymentFee)

        // When
        orderSummaryPageViewModel.calculateTotal()

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    3000.0,
                    1000.0,
                    500.0,
                    totalItemPriceAndShippingFee = 1500.0,
                    totalPriceWithoutDiscountsAndPaymentFees = 1500.0,
                    totalPriceWithoutPaymentFees = 1500.0,
                    isInstallment = true,
                    orderPaymentFees = helper.paymentFeeDetails
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }

    @Test
    fun `Calculate Total Credit Card With Dynamic Payment Fee Details`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(OrderProduct(orderQuantity = 1, productPrice = 1000.0)))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = OrderShipment(shippingPrice = 500, shipperProductId = 1, serviceName = "service")
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(
            isEnable = true,
            creditCard = OrderPaymentCreditCard(
                numberOfCards = OrderPaymentCreditCardsNumber(1, 0, 1),
                availableTerms = listOf(
                    OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100),
                    OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isError = true)
                ),
                selectedTerm = OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isError = true)
            ),
            originalPaymentFees = helper.paymentFeeDetails
        )

        // When
        orderSummaryPageViewModel.calculateTotal(skipDynamicFee = true)

        // Then
        assertEquals(
            OrderTotal(
                OrderCost(
                    3045.0, 1000.0, 500.0, paymentFee = 45.0,
                    totalItemPriceAndShippingFee = 1500.0, totalPriceWithoutDiscountsAndPaymentFees = 1500.0, totalPriceWithoutPaymentFees = 1500.0, isInstallment = true, orderPaymentFees = helper.paymentFeeDetails
                ),
                OccButtonState.DISABLE,
                OccButtonType.PAY
            ),
            orderSummaryPageViewModel.orderTotal.value
        )
    }
}
