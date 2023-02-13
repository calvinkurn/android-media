package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccData
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccPaymentParameter
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccRedirectParam
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccResult
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OccPrompt
import com.tokopedia.oneclickcheckout.order.view.model.OrderInsurance
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileAddress
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.BenefitSummaryInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ErrorDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderSummaryPageViewModelPromoTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Generate Promo Request With Insurance`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(insurance = OrderInsurance(isCheckInsurance = true, insuranceData = helper.firstCourierFirstDuration.productData.insurance))

        // When
        val promoRequest = orderSummaryPageViewModel.generatePromoRequest()

        // Then
        assertEquals(1, promoRequest.orders.first().isInsurancePrice)
    }

    @Test
    fun `Generate Promo Request With Insurance Checked And No Data`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(insurance = OrderInsurance(isCheckInsurance = true))

        // When
        val promoRequest = orderSummaryPageViewModel.generatePromoRequest()

        // Then
        assertEquals(0, promoRequest.orders.first().isInsurancePrice)
    }

    @Test
    fun `Generate Validate Use Promo Request With Last Apply And Bbo`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(address = OrderProfileAddress())
        val promoCode = "123"
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(
            lastApply = LastApplyUiModel(
                codes = listOf(promoCode),
                voucherOrders = listOf(LastApplyVoucherOrdersItemUiModel(code = promoCode))
            )
        )
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(isApplyLogisticPromo = true, logisticPromoViewModel = helper.logisticPromo, logisticPromoShipping = helper.firstCourierSecondDuration)

        // When
        val promoRequest = orderSummaryPageViewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(promoCode, promoRequest.codes.first())
        assertEquals(listOf(promoCode, helper.logisticPromo.promoCode), promoRequest.orders.first().codes)
    }

    @Test
    fun `Generate Validate Use Promo Request With Invalid Last Apply And No Bbo`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val promoCode = "123"
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(lastApply = LastApplyUiModel(voucherOrders = listOf(LastApplyVoucherOrdersItemUiModel(code = promoCode, uniqueId = promoCode))))
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(logisticPromoViewModel = null, logisticPromoTickerMessage = null)

        // When
        val promoRequest = orderSummaryPageViewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(true, promoRequest.codes.isEmpty())
        assertEquals(true, promoRequest.orders.first().codes.isEmpty())
    }

    @Test
    fun `Generate Validate Use Promo Request With Multiple Last Apply`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        val promoCode = "123"
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(
            lastApply = LastApplyUiModel(
                voucherOrders = listOf(LastApplyVoucherOrdersItemUiModel(code = promoCode), LastApplyVoucherOrdersItemUiModel(code = promoCode))
            )
        )
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        // When
        val promoRequest = orderSummaryPageViewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(true, promoRequest.codes.isEmpty())
        assertEquals(listOf(promoCode), promoRequest.orders.first().codes)
    }

    @Test
    fun `Update Cart Promo Success Without Promo`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        var validateUsePromoRequest = ValidateUsePromoRequest()
        var promoRequest = PromoRequest()
        var bboCodes = emptyList<String>()
        orderSummaryPageViewModel.updateCartPromo { resultValidateUsePromoRequest, resultPromoRequest, resultBboCodes ->
            validateUsePromoRequest = resultValidateUsePromoRequest
            promoRequest = resultPromoRequest
            bboCodes = resultBboCodes
        }

        // Then
        assertEquals(
            ValidateUsePromoRequest(
                isSuggested = 0,
                skipApply = 0,
                cartType = "occmulti",
                state = "checkout",
                orders = listOf(
                    OrdersItem(
                        shippingId = helper.firstCourierFirstDuration.productData.shipperId,
                        spId = helper.firstCourierFirstDuration.productData.shipperProductId,
                        shopId = helper.orderData.cart.shop.shopId.toLongOrZero(),
                        productDetails = listOf(ProductDetailsItem(helper.product.orderQuantity, helper.product.productId.toLongOrZero()))
                    )
                )
            ),
            validateUsePromoRequest
        )
        assertEquals(
            PromoRequest(
                cartType = "occmulti",
                state = "checkout",
                orders = listOf(
                    Order(
                        isChecked = true,
                        shippingId = helper.firstCourierFirstDuration.productData.shipperId,
                        spId = helper.firstCourierFirstDuration.productData.shipperProductId,
                        shopId = helper.orderData.cart.shop.shopId.toLongOrZero(),
                        product_details = listOf(ProductDetail(helper.product.productId.toLongOrZero(), helper.product.orderQuantity))
                    )
                )
            ),
            promoRequest
        )
        assertEquals(0, bboCodes.size)
    }

    @Test
    fun `Update Cart Promo Success With BBO`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } returns ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(code = "bbo", messageUiModel = MessageUiModel(state = "green"))
                ),
                globalSuccess = true
            ),
            status = "OK", errorCode = "200"
        )
        orderSummaryPageViewModel.chooseLogisticPromo(helper.logisticPromo)
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        var validateUsePromoRequest = ValidateUsePromoRequest()
        var promoRequest = PromoRequest()
        var bboCodes = emptyList<String>()
        orderSummaryPageViewModel.updateCartPromo { resultValidateUsePromoRequest, resultPromoRequest, resultBboCodes ->
            validateUsePromoRequest = resultValidateUsePromoRequest
            promoRequest = resultPromoRequest
            bboCodes = resultBboCodes
        }

        // Then
        assertEquals(
            ValidateUsePromoRequest(
                isSuggested = 0,
                skipApply = 0,
                cartType = "occmulti",
                state = "checkout",
                orders = listOf(
                    OrdersItem(
                        shippingId = helper.logisticPromo.shipperId,
                        spId = helper.logisticPromo.shipperProductId,
                        freeShippingMetadata = helper.logisticPromo.freeShippingMetadata,
                        codes = mutableListOf(helper.logisticPromo.promoCode),
                        shippingPrice = 2000.0,
                        shopId = helper.orderData.cart.shop.shopId.toLongOrZero(),
                        productDetails = listOf(ProductDetailsItem(helper.product.orderQuantity, helper.product.productId.toLongOrZero()))
                    )
                )
            ),
            validateUsePromoRequest
        )
        assertEquals(
            PromoRequest(
                cartType = "occmulti",
                state = "checkout",
                orders = listOf(
                    Order(
                        isChecked = true,
                        shippingId = helper.logisticPromo.shipperId,
                        spId = helper.logisticPromo.shipperProductId,
                        freeShippingMetadata = helper.logisticPromo.freeShippingMetadata,
                        codes = mutableListOf(helper.logisticPromo.promoCode),
                        shopId = helper.orderData.cart.shop.shopId.toLongOrZero(),
                        product_details = listOf(ProductDetail(helper.product.productId.toLongOrZero(), helper.product.orderQuantity))
                    )
                )
            ),
            promoRequest
        )
        assertEquals(1, bboCodes.size)
    }

    @Test
    fun `Update Cart Promo Got Prompt`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        val occPrompt = OccPrompt()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns occPrompt

        // When
        orderSummaryPageViewModel.updateCartPromo { _, _, _ -> }

        // Then
        assertEquals(OccGlobalEvent.Prompt(occPrompt), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Cart Promo Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        val response = Throwable()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } throws response

        // When
        orderSummaryPageViewModel.updateCartPromo { _, _, _ -> }

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Update Cart Promo Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(address = OrderProfileAddress())
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        // When
        orderSummaryPageViewModel.updateCartPromo { _, _, _ -> }

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Validate Use Promo Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        val promoCode = "abc"
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        val response = ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200", promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "green")))
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } returns response

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        verify(inverse = true) {
            orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
        }
        assertEquals(response, orderSummaryPageViewModel.validateUsePromoRevampUiModel)
        assertEquals(OccButtonState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Validate Use Promo Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        val promoCode = "abc"
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } throws Throwable()

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderPromo.value.state)
        assertEquals(OccButtonState.DISABLE, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Validate Use Promo Error Akamai`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        val promoCode = "abc"
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        val exception = AkamaiErrorException("")
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } throws exception
        coEvery { clearCacheAutoApplyStackUseCase.get().setParams(any()).executeOnBackground() } returns ClearPromoUiModel()

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        assertEquals(OrderPromo(state = OccButtonState.NORMAL), orderSummaryPageViewModel.orderPromo.value)
        assertEquals(OccButtonState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
        assertEquals(false, orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        assertEquals(OccGlobalEvent.Error(exception), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Validate Use Promo Red State Released`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200", promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "red")))
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } returns response
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
        }
        assertEquals(response, orderSummaryPageViewModel.validateUsePromoRevampUiModel)
        assertEquals(OccButtonState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Validate Use Promo Red State Released And Has newGlobalEvent`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        val promoCode = "abc"
        val errorMessage = "error message"
        val response = ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                codes = listOf(promoCode),
                messageUiModel = MessageUiModel(state = "red"),
                additionalInfoUiModel = AdditionalInfoUiModel(
                    errorDetailUiModel = ErrorDetailUiModel(
                        message = errorMessage
                    )
                )
            )
        )
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } returns response
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
        }
        assertEquals(response, orderSummaryPageViewModel.validateUsePromoRevampUiModel)
        assertEquals(OccButtonState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
        assertEquals(OccGlobalEvent.ToasterInfo(errorMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Validate Use Promo Benefit Decreased`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(
            status = "OK", errorCode = "200",
            promoUiModel = PromoUiModel(
                codes = listOf(promoCode),
                messageUiModel = MessageUiModel(state = "green"),
                benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(finalBenefitAmount = 10),
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(messageUiModel = MessageUiModel(state = "green")))
            )
        )
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } returns response
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(
            promoUiModel = PromoUiModel(
                messageUiModel = MessageUiModel(state = "green"),
                benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(finalBenefitAmount = 1000),
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(messageUiModel = MessageUiModel(state = "green")))
            )
        )

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        verify(exactly = 1) {
            orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(false)
        }
        assertEquals(response, orderSummaryPageViewModel.validateUsePromoRevampUiModel)
        assertEquals(OccButtonState.NORMAL, orderSummaryPageViewModel.orderTotal.value.buttonState)
    }

    @Test
    fun `Validate Use With No Promo Code But With Usage Summaries`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(
            lastApply = LastApplyUiModel(
                additionalInfo = LastApplyAdditionalInfoUiModel(
                    usageSummaries = listOf(LastApplyUsageSummariesUiModel())
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateUsePromo()

        // Then
        assertEquals(LastApplyUiModel(), orderSummaryPageViewModel.orderPromo.value.lastApply)
    }

    @Test
    fun `Final Validate Use Promo Global Code Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
        val promoCode = "abc"
        val response = ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200", promoUiModel = PromoUiModel(codes = listOf(promoCode), messageUiModel = MessageUiModel(state = "green")))
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } returns response
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({ }, false)

        // Then
        coVerify {
            checkoutOccUseCase.executeSuspend(
                match {
                    val globalCode = it.carts.promos.first()
                    globalCode.code == promoCode && globalCode.type == "global" && it.carts.data.first().shopProducts.first().promos.isEmpty()
                }
            )
        }
    }

    @Test
    fun `Final Validate Use Promo Voucher Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
        val promoCode = "abc"
        val promoType = "type"
        val response = ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200", promoUiModel = PromoUiModel(voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(code = promoCode, type = promoType, messageUiModel = MessageUiModel(state = "green")))))
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } returns response
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(codes = mutableListOf(promoCode))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = response.copy(promoUiModel = response.promoUiModel.copy(messageUiModel = MessageUiModel(state = "green")))
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({ }, false)

        // Then
        coVerify {
            checkoutOccUseCase.executeSuspend(
                match {
                    val voucherCode = it.carts.data.first().shopProducts.first().promos.first()
                    voucherCode.code == promoCode && voucherCode.type == promoType && it.carts.promos.isEmpty()
                }
            )
        }
    }

    @Test
    fun `Final Validate Use Promo Error Akamai`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart.copy(shop = OrderShop(isGold = 1))
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
        val lastResponse = ValidateUsePromoRevampUiModel(
            status = "OK", errorCode = "200",
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )
        val response = AkamaiErrorException("")
        coEvery { validateUsePromoRevampUseCase.get().setParam(any()).executeOnBackground() } throws response
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(mutableListOf("promo"))
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = lastResponse
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        coEvery { clearCacheAutoApplyStackUseCase.get().setParams(any()).executeOnBackground() } returns ClearPromoUiModel()

        // When
        orderSummaryPageViewModel.finalUpdate({ }, false)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(throwable = response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Cancel Ineligible Promo Checkout Success`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderPromo.value = OrderPromo(state = OccButtonState.NORMAL)
        coEvery { clearCacheAutoApplyStackUseCase.get().setParams(any()).executeOnBackground() } returns ClearPromoUiModel()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isSuccess = false
        orderSummaryPageViewModel.cancelIneligiblePromoCheckout(arrayListOf()) {
            isSuccess = true
        }

        // Then
        assertEquals(true, isSuccess)
    }

    @Test
    fun `Cancel Ineligible Promo Checkout Error`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        val response = Throwable()
        coEvery { clearCacheAutoApplyStackUseCase.get().setParams(any()).executeOnBackground() } throws response
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isSuccess = false
        orderSummaryPageViewModel.cancelIneligiblePromoCheckout(arrayListOf()) {
            isSuccess = true
        }

        // Then
        assertEquals(false, isSuccess)
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Auto un-apply bbo when bo unstacking`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = helper.firstCourierSecondDuration
        )
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem(
                    uniqueId = helper.orderData.cart.cartString
                )
            )
        }

        // When
        orderSummaryPageViewModel.autoUnApplyBBO()

        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `Auto un-apply bbo failed when last validate use promo request orders empty`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = helper.firstCourierSecondDuration
        )
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest()

        // When
        orderSummaryPageViewModel.autoUnApplyBBO()

        // Then
        assertTrue(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `Auto un-apply bbo failed when last validate use promo request orders unique id not match with cart string`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart.apply { cartString = "test" }
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = helper.firstCourierSecondDuration
        )
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest().apply {
            orders = listOf(
                OrdersItem(
                    uniqueId = "untest"
                )
            )
        }

        // When
        orderSummaryPageViewModel.autoUnApplyBBO()

        // Then
        assertTrue(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `Auto un-apply bbo when bo stacking`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = helper.firstCourierSecondDuration
        )
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest()
            .apply {
                orders = listOf(
                    OrdersItem(
                        uniqueId = helper.orderData.cart.cartString,
                        codes = mutableListOf(helper.logisticPromo.promoCode)
                    )
                )
            }

        // When
        orderSummaryPageViewModel.autoUnApplyBBO()

        // Then
        assertTrue(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `Auto un-apply bbo when last validate promo order empty`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = helper.firstCourierSecondDuration
        )
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest()

        // When
        orderSummaryPageViewModel.autoUnApplyBBO()

        // Then
        assertTrue(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `Auto un-apply bbo when last validate promo order null`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = helper.firstCourierSecondDuration
        )
        orderSummaryPageViewModel.lastValidateUsePromoRequest = null

        // When
        orderSummaryPageViewModel.autoUnApplyBBO()

        // Then
        assertTrue(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    // case
    // apply bo
    @Test
    fun `Apply Bbo promo with same code with order logistic promo voucher code`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertTrue(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        assertEquals(
            "bbo",
            orderSummaryPageViewModel.orderShipment.value.logisticPromoViewModel!!.promoCode
        )
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.Normal)
    }

    @Test
    fun `Apply Bbo promo same code with order logistic promo voucher code when ordershipment is applied logistic promo`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(isApplyLogisticPromo = true)
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertTrue(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        assertEquals(
            "bbo",
            orderSummaryPageViewModel.orderShipment.value.logisticPromoViewModel!!.promoCode
        )
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.Normal)
    }

    @Test
    fun `Apply Bbo promo when different voucher code applied with order shipment logistic promo code`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bba",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertTrue(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        assertEquals(
            "bba",
            orderSummaryPageViewModel.orderShipment.value.logisticPromoViewModel!!.promoCode
        )
    }

    @Test
    fun `Apply Bbo promo when different voucher code applied with order shipment logistic promo code and null logistic promo view model`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(logisticPromoViewModel = null)
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bba",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertTrue(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        assertEquals(
            "bba",
            orderSummaryPageViewModel.orderShipment.value.logisticPromoViewModel!!.promoCode
        )
    }

    // un-apply bo
    @Test
    fun `test has apply promo logistic but no voucher exist for that code`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(isApplyLogisticPromo = true)
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bba",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    )
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        assertEquals(
            "bbo",
            orderSummaryPageViewModel.orderShipment.value.logisticPromoViewModel!!.promoCode
        )
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.AdjustShippingToaster)
    }

    @Test
    fun `ignored Bbo promo`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    )
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.Normal)
    }

    @Test
    fun `test red voucher promo makes bo un-applied`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(isApplyLogisticPromo = true)
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    )
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.AdjustShippingToaster)
    }

    @Test
    fun `validate bbo stacking when logistic promo view model null`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value =
            helper.orderShipment.copy(isApplyLogisticPromo = true, logisticPromoViewModel = null)
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "red")
                    )
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.Normal)
    }

    // show toaster
    @Test
    fun `show bo stacking toaster error message`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = helper.firstCourierSecondDuration
        )
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            PromoUiModel(
                additionalInfoUiModel = AdditionalInfoUiModel(
                    errorDetailUiModel = ErrorDetailUiModel(
                        message = "error clashing"
                    )
                )
            )
        )
        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertEquals(
            "error clashing",
            (orderSummaryPageViewModel.globalEvent.value as OccGlobalEvent.ToasterInfo).message
        )
    }

    // show toaster for un-apply bo
    @Test
    fun `show shipping adjustment toaster`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = helper.firstCourierSecondDuration
        )
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel()
        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.AdjustShippingToaster)
    }

    @Test
    fun `don't show any toaster because validate use promo revamp ui model null`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = helper.firstCourierSecondDuration
        )
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = null
        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.Normal)
    }

    @Test
    fun `don't show any toaster because error detail ui model message blank`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(
            isApplyLogisticPromo = true,
            logisticPromoViewModel = helper.logisticPromo,
            logisticPromoShipping = helper.firstCourierSecondDuration
        )
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                ),
                additionalInfoUiModel = AdditionalInfoUiModel(
                    errorDetailUiModel = ErrorDetailUiModel(
                        message = " "
                    )
                )
            )
        )
        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.Normal)
    }

    // no promo bo
    @Test
    fun `un-Apply Bbo promo`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        shippingId = 1,
                        spId = 1,
                        messageUiModel = MessageUiModel(state = "yellow")
                    )
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `didn't show any toaster if no promo un-apply or unstacked`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "bbo",
                        shippingId = 1,
                        spId = 1,
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertTrue(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.Normal)
    }

    @Test
    fun `didn't show any toaster if no promo validate`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(isApplyLogisticPromo = false)
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = null

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertTrue(orderSummaryPageViewModel.globalEvent.value is OccGlobalEvent.Normal)
    }

    @Test
    fun `validate bo stacking when promo checkout green but not logistic promo`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(messageUiModel = MessageUiModel(state = "green")))
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `validate bo stacking when promo checkout green but not logistic promo but has shipping id`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(shippingId = 1, messageUiModel = MessageUiModel(state = "green")))
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `validate bo stacking when promo checkout green but not logistic promo but has sp id`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(spId = 1, messageUiModel = MessageUiModel(state = "green")))
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `validate bo stacking when promo checkout message state neither green or red`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel(
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(shippingId = 1, spId = 1, messageUiModel = MessageUiModel(state = "yellow")))
            )
        )

        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `validate bo stacking when validate use promo revamp null`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = null
        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `validate bo stacking when voucher promo null`() {
        // Given
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.validateUsePromoRevampUiModel = ValidateUsePromoRevampUiModel()
        orderSummaryPageViewModel.validateUsePromoRevampUiModel?.promoUiModel?.voucherOrderUiModels =
            emptyList()
        // When
        orderSummaryPageViewModel.validateBboStacking()
        // Then
        assertFalse(orderSummaryPageViewModel.orderShipment.value.isApplyLogisticPromo)
    }

    @Test
    fun `test update promo state without calculate`() {
        // Given
        val promoUiModel = PromoUiModel()
        orderSummaryPageViewModel.orderPromo.value =
            orderSummaryPageViewModel.orderPromo.value.copy(isDisabled = true)
        // When
        orderSummaryPageViewModel.updatePromoStateWithoutCalculate(promoUiModel)
        // Then
        assertEquals(
            orderSummaryPageViewModel.orderPromo.value.lastApply,
            LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel)
        )
        assertFalse(orderSummaryPageViewModel.orderPromo.value.isDisabled)
    }
}
