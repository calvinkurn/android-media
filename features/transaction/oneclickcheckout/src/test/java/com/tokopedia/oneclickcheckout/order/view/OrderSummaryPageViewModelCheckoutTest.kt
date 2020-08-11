package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.data.checkout.*
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
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl"))))))
        }

        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        assertEquals(true, isOnSuccessCalled)
        assertEquals(OccGlobalEvent.Loading, orderSummaryPageViewModel.globalEvent.value)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout Success With Transaction Id`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val transactionId = "123"
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl", form = "transaction_id=$transactionId&success=1"))))))
        }

        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), transactionId, any()) }
    }

    @Test
    fun `Checkout Success With Transaction Id As Last Query`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val transactionId = "123"
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl", form = "transaction_id=$transactionId"))))))
        }

        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), transactionId, any()) }
    }

    @Test
    fun `Checkout Success With Invalid Transaction Id Value`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl", form = "transaction_id="))))))
        }

        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), "", any()) }
    }

    @Test
    fun `Checkout Success With Invalid Transaction Id Query`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl", form = "transaction_id_error=1"))))))
        }

        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), "", any()) }
    }

    @Test
    fun `Checkout Success Using Insurance`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment.copy(isCheckInsurance = true)
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(match { it.carts.data.first().shopProducts.first().finsurance == 1 }, any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl"))))))
        }

        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout Success Mode 0`() {
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
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl"))))))
        }

        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout Success Mode 1`() {
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
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl"))))))
        }

        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout Success Using Promos`() {
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
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl"))))))
        }

        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        }, false)

        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout On Invalid Shipping State`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout On Invalid Preference State`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = false)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout On Invalid Profile Id`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = OrderProfile(), isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout On Invalid Credit Card State`() {
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

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.Error(errorMessage = DEFAULT_LOCAL_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout On Invalid Button State`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.LOADING)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        verify(inverse = true) { updateCartOccUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `Checkout Error`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 0, error = Error(ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY)))))
        }

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.CheckoutError(CheckoutErrorData(ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY)), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Price Change Error`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 0, error = Error(ERROR_CODE_PRICE_CHANGE)))))
        }

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.PriceChangeError(PriceChangeMessage(PRICE_CHANGE_ERROR_MESSAGE, "", PRICE_CHANGE_ACTION_MESSAGE)), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Unknown Error`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val errorMessage = "unknown error"
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 0, error = Error("000", message = errorMessage)))))
        }

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = errorMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Unknown Error Default Message`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 0, error = Error("000")))))
        }

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = "Terjadi kesalahan dengan kode 000"), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Status Error`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        val responseMessage = "error"
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = "", header = Header(messages = listOf(responseMessage)))))
        }

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = responseMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Status Error Default Message`() {
        orderSummaryPageViewModel.orderTotal.value = OrderTotal(buttonState = ButtonBayarState.NORMAL)
        orderSummaryPageViewModel._orderPreference = OrderPreference(preference = helper.preference, isValid = true)
        orderSummaryPageViewModel._orderShipment = helper.orderShipment
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(ValidateUsePromoRevampUiModel())
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = "")))
        }

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = DEFAULT_ERROR_MESSAGE), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Failed`() {
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

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        }, false)

        assertEquals(OccGlobalEvent.Error(response), orderSummaryPageViewModel.globalEvent.value)
    }
}