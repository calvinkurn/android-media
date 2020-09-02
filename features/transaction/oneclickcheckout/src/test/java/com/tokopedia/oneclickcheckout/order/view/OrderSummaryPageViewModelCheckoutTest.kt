package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartDataOcc
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccGqlResponse
import com.tokopedia.oneclickcheckout.order.data.update.UpdateCartOccResponse
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.ERROR_CODE_PRICE_CHANGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.PRICE_CHANGE_ACTION_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.PRICE_CHANGE_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.ErrorCheckoutBottomSheet
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class OrderSummaryPageViewModelCheckoutTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Checkout Success`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl")))))
        }

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        assertEquals(OccGlobalEvent.Loading, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout Success With Transaction Id`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val transactionId = "123"
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl", form = "transaction_id=$transactionId&success=1")))))
        }

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), transactionId, any()) }
    }

    @Test
    fun `Checkout Success With Transaction Id As Last Query`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val transactionId = "123"
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl", form = "transaction_id=$transactionId")))))
        }

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), transactionId, any()) }
    }

    @Test
    fun `Checkout Success With Invalid Transaction Id Value`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl", form = "transaction_id=")))))
        }

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), "", any()) }
    }

    @Test
    fun `Checkout Success With Invalid Transaction Id Query`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl", form = "transaction_id_error=1")))))
        }

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), "", any()) }
    }

    @Test
    fun `Checkout Success Using Insurance`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(isCheckInsurance = true)
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(match { it.carts.data.first().shopProducts.first().finsurance == 1 }, any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl")))))
        }

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout Success Mode 0`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL, isButtonChoosePayment = false)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every {
            checkoutOccUseCase.execute(match { it.carts.mode == 0 }, any(), any())
        } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl")))))
        }

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout Success Mode 1`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL, isButtonChoosePayment = true)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every {
            checkoutOccUseCase.execute(match { it.carts.mode == 1 }, any(), any())
        } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl")))))
        }

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout Success Using Promos`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        val promoUiModel = PromoUiModel(codes = listOf("promo"), messageUiModel = MessageUiModel(state = "green"),
                voucherOrderUiModels = listOf(PromoCheckoutVoucherOrdersItemUiModel(code = "promo", type = "type", messageUiModel = MessageUiModel(state = "green"))))
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel(promoUiModel))
        every {
            checkoutOccUseCase.execute(match {
                val globalCode = it.carts.promos.first()
                val voucherCode = it.carts.data.first().shopProducts.first().promos.first()
                globalCode.code == "promo" && globalCode.type == "global" && voucherCode.code == "promo" && voucherCode.type == "type"
            }, any(), any())
        } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 1, paymentParameter = CheckoutOccPaymentParameter(redirectParam = CheckoutOccRedirectParam(url = "testurl")))))
        }

        // When
        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        // Then
        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout On Invalid Shipping State`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout On Invalid Profile Id`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = OrderProfile(), isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        var preference = helper.preference
        preference = preference.copy(payment = preference.payment.copy(metadata = """
            {
                "express_checkout_param" : {}
            }
        """.trimIndent()))
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true, creditCard = OrderPaymentCreditCard(selectedTerm = OrderPaymentInstallmentTerm()))

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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.LOADING)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `Checkout Error Prompt`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val prompt = OccPrompt(OccPrompt.FROM_CHECKOUT, OccPrompt.TYPE_DIALOG)
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 0, prompt = prompt)))
        }

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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 0, error = CheckoutOccErrorData(ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY))))
        }

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.CheckoutError(CheckoutOccErrorData(ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY)), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Price Change Error`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 0, error = CheckoutOccErrorData(ERROR_CODE_PRICE_CHANGE))))
        }

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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val errorMessage = "unknown error"
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 0, error = CheckoutOccErrorData("000", message = errorMessage))))
        }

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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = STATUS_OK, result = CheckoutOccResult(success = 0, error = CheckoutOccErrorData("000"))))
        }

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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val responseMessage = "error"
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = "", headerMessage = responseMessage))
        }

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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccData) -> Unit)).invoke(CheckoutOccData(status = ""))
        }

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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val response = Throwable()
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (thirdArg() as ((Throwable) -> Unit)).invoke(response)
        }

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout With Error Installment`() {
        // Given
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference.copy(
                payment = OrderProfilePayment(
                        metadata = """{"express_checkout_param":{"installment_term":"3"}}""")),
                isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        orderSummaryPageViewModel._orderPayment = OrderPayment(isEnable = true,
                creditCard = OrderPaymentCreditCard(
                        numberOfCards = OrderPaymentCreditCardsNumber(1, 0, 1),
                        availableTerms = listOf(
                                OrderPaymentInstallmentTerm(term = 0, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100),
                                OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isEnable = false)
                        ),
                        selectedTerm = OrderPaymentInstallmentTerm(term = 3, mdr = 1.5f, mdrSubsidize = 0.5f, minAmount = 100000, isSelected = true, isEnable = false)
                )
        )
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }

        // When
        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        // Then
        assertEquals(OccGlobalEvent.Error(errorMessage = OrderSummaryPageViewModel.INSTALLMENT_INVALID_MIN_AMOUNT), orderSummaryPageViewModel.globalEvent.value)
    }
}