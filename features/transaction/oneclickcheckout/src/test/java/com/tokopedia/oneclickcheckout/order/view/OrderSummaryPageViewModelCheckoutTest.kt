package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryPageEnhanceECommerce
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.ERROR_CODE_PRICE_CHANGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.PRICE_CHANGE_ACTION_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.PRICE_CHANGE_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccData
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccErrorData
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccPaymentParameter
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccRedirectParam
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccResult
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonType
import com.tokopedia.oneclickcheckout.order.view.model.OccPrompt
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderInsurance
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCard
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCardsNumber
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentInstallmentTerm
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfilePayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.oneclickcheckout.order.view.model.PriceChangeMessage
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable
import java.io.IOException

@ExperimentalCoroutinesApi
class OrderSummaryPageViewModelCheckoutTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Checkout Success`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        assertEquals(OccGlobalEvent.Loading, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any(), any(), any()) }
        verify(inverse = true) { orderSummaryAnalytics.eventPPClickBayar(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `Checkout Success With Transaction Id`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val transactionId = "123"
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl", form = "transaction_id=$transactionId&success=1"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), transactionId, any(), any()) }
    }

    @Test
    fun `Checkout Success With Transaction Id And Payment Type`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        val paymentType = "paymentType"
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(payment = OrderProfilePayment(gatewayName = paymentType, gatewayCode = "payment"))
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val transactionId = "123"
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl", form = "transaction_id=$transactionId&success=1"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), transactionId, paymentType, any()) }
    }

    @Test
    fun `Checkout Success With Transaction Id And Default Payment Type`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val transactionId = "123"
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl", form = "transaction_id=$transactionId&success=1"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), transactionId, OrderSummaryPageEnhanceECommerce.DEFAULT_EMPTY_VALUE, any()) }
    }

    @Test
    fun `Checkout Success With Transaction Id As Last Query`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val transactionId = "123"
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl", form = "transaction_id=$transactionId"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), transactionId, any(), any()) }
    }

    @Test
    fun `Checkout Success With Invalid Transaction Id Value`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl", form = "transaction_id="))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), "", any(), any()) }
    }

    @Test
    fun `Checkout Success With Invalid Transaction Id Query`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl", form = "transaction_id_error=1"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), "", any(), any()) }
    }

    @Test
    fun `Checkout Success Using Insurance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment.copy(insurance = OrderInsurance(isCheckInsurance = true))
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery { checkoutOccUseCase.executeSuspend(match { it.carts.data.first().shopProducts.first().finsurance == 1 }) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `Checkout Success Mode 0`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL, buttonType = OccButtonType.PAY)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery {
            checkoutOccUseCase.executeSuspend(match { it.carts.mode == 0 })
        } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `Checkout Success Mode 1`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL, buttonType = OccButtonType.CHOOSE_PAYMENT)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery {
            checkoutOccUseCase.executeSuspend(match { it.carts.mode == 1 })
        } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `Checkout Success Using Promos`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.lastValidateUsePromoRequest = ValidateUsePromoRequest(mutableListOf("promo"))
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        val promoUiModel = PromoUiModel(codes = listOf("promo"), messageUiModel = MessageUiModel(state = "green"),
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(code = "promo", type = "type", messageUiModel = MessageUiModel(state = "green"))))
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(promoUiModel))
        coEvery {
            checkoutOccUseCase.executeSuspend(match {
                val globalCode = it.carts.promos.first()
                val voucherCode = it.carts.data.first().shopProducts.first().promos.first()
                globalCode.code == "promo" && globalCode.type == "global" && voucherCode.code == "promo" && voucherCode.type == "type"
            })
        } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `Checkout On Invalid Shipping State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout On Invalid Preference State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(payment = OrderProfilePayment())
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout On Invalid Address Id`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = OrderProfile()
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout On Invalid Credit Card State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {}
            }
        """.trimIndent()))
        orderSummaryPageViewModel.orderProfile.value = preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(selectedTerm = OrderPaymentInstallmentTerm()))

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout On Invalid Button State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.LOADING)
        orderSummaryPageViewModel.orderProfile.value = helper.preference

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        coVerify(inverse = true) { updateCartOccUseCase.executeSuspend(any()) }
    }

    @Test
    fun `Checkout Error Prompt`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val prompt = OccPrompt(OccPrompt.TYPE_DIALOG)
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 0, prompt = prompt))

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Prompt(prompt), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val errorMessage = "checkout error"
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 0, error = CheckoutOccErrorData(message = errorMessage)))

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = errorMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Error Update Cart`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val exception = IOException()
        coEvery { updateCartOccUseCase.executeSuspend(any()) } throws exception

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(throwable = exception), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Failed Update Cart`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        val errorMessage = "cart error"
        coEvery { updateCartOccUseCase.executeSuspend(any()) } throws MessageErrorException(errorMessage)

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = errorMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Price Change Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 0, error = CheckoutOccErrorData(ERROR_CODE_PRICE_CHANGE)))

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.PriceChangeError(PriceChangeMessage(PRICE_CHANGE_ERROR_MESSAGE, "", PRICE_CHANGE_ACTION_MESSAGE)), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Unknown Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val errorMessage = "unknown error"
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 0, error = CheckoutOccErrorData("000", message = errorMessage)))

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = errorMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Unknown Error Default Message`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 0, error = CheckoutOccErrorData("000")))

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = "Terjadi kesalahan dengan kode 000"), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Status Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val responseMessage = "error"
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = "", headerMessage = responseMessage)

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = responseMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Status Error Default Message`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = "")

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = DEFAULT_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Failed`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val response = Throwable()
        coEvery { checkoutOccUseCase.executeSuspend(any()) } throws response

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout With Some Disable Installment`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(
                payment = OrderProfilePayment(metadata = """{"express_checkout_param":{"installment_term":"3"}}""", gatewayCode = "payment"))
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true,
                creditCard = OrderPaymentCreditCard(
                        numberOfCards = OrderPaymentCreditCardsNumber(1, 0, 1),
                        availableTerms = listOf(
                                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, isEnable = true),
                                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isEnable = false)
                        ),
                        selectedTerm = OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isEnable = false)
                )
        )
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = OrderSummaryPageViewModel.INSTALLMENT_INVALID_MIN_AMOUNT), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout With All Disable Installment`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference.copy(
                payment = OrderProfilePayment(metadata = """{"express_checkout_param":{"installment_term":"3"}}""", gatewayCode = "payment"))
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = helper.orderData.cart
        orderSummaryPageViewModel.orderPayment.value = OrderPayment(isEnable = true,
                creditCard = OrderPaymentCreditCard(
                        numberOfCards = OrderPaymentCreditCardsNumber(1, 0, 1),
                        availableTerms = listOf(
                                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100, isEnable = false),
                                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isEnable = false)
                        ),
                        selectedTerm = OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isEnable = false)
                )
        )
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `Checkout Success With PPP Ticked`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(helper.product.copy(
                purchaseProtectionPlanData = PurchaseProtectionPlanData(
                        isProtectionAvailable = true,
                        stateChecked = PurchaseProtectionPlanData.STATE_TICKED
                )))
        )
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventPPClickBayar(any(), any(), any(), any(), true, any()) }
    }

    @Test
    fun `Checkout Success With PPP Unticked`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = OccButtonState.NORMAL)
        orderSummaryPageViewModel.orderProfile.value = helper.preference
        orderSummaryPageViewModel.orderShipment.value = helper.orderShipment
        orderSummaryPageViewModel.orderCart = OrderCart(products = mutableListOf(helper.product.copy(
                purchaseProtectionPlanData = PurchaseProtectionPlanData(
                        isProtectionAvailable = true,
                        stateChecked = PurchaseProtectionPlanData.STATE_UNTICKED
                )))
        )
        coEvery { updateCartOccUseCase.executeSuspend(any()) } returns null
        every { validateUsePromoRevampUseCase.get().createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        coEvery { checkoutOccUseCase.executeSuspend(any()) } returns CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl"))))

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventPPClickBayar(any(), any(), any(), any(), false, any()) }
    }
}