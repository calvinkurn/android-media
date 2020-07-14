package com.tokopedia.oneclickcheckout.order.view

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
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutErrorData
import com.tokopedia.oneclickcheckout.order.view.model.PriceChangeMessage
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderSummaryPageViewModelCheckoutTest : BaseOrderSummaryPageViewModelTest() {

    @Test
    fun `Checkout Success`() {
        setUpCartAndRates()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 1, paymentParameter = PaymentParameter(redirectParam = RedirectParam(url = "testurl"))))))
        }

        var isOnSuccessCalled = false
        orderSummaryPageViewModel.finalUpdate({
            isOnSuccessCalled = true
        })

        assertEquals(true, isOnSuccessCalled)
        verify(exactly = 1) { orderSummaryAnalytics.eventClickBayarSuccess(any(), any(), any()) }
    }

    @Test
    fun `Checkout Error`() {
        setUpCartAndRates()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 0, error = Error(ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY)))))
        }

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        })

        assertEquals(OccGlobalEvent.CheckoutError(CheckoutErrorData(ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY)), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Price Change Error`() {
        setUpCartAndRates()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 0, error = Error(ERROR_CODE_PRICE_CHANGE)))))
        }

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        })

        assertEquals(OccGlobalEvent.PriceChangeError(PriceChangeMessage(PRICE_CHANGE_ERROR_MESSAGE, "", PRICE_CHANGE_ACTION_MESSAGE)), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Price Unknown Error`() {
        setUpCartAndRates()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        val errorMessage = "unknown error"
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 0, error = Error("000", message = errorMessage)))))
        }

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        })

        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = errorMessage), orderSummaryPageViewModel.globalEvent.value)
    }

    @Test
    fun `Checkout Price Unknown Error Default Message`() {
        setUpCartAndRates()
        every { updateCartOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((UpdateCartOccGqlResponse) -> Unit)).invoke(UpdateCartOccGqlResponse(UpdateCartOccResponse(data = UpdateCartDataOcc())))
        }
        every { checkoutOccUseCase.execute(any(), any(), any()) } answers {
            (secondArg() as ((CheckoutOccGqlResponse) -> Unit)).invoke(CheckoutOccGqlResponse(CheckoutOccResponse(status = STATUS_OK, data = Data(success = 0, error = Error("000")))))
        }

        orderSummaryPageViewModel.finalUpdate({
            //do nothing
        })

        assertEquals(OccGlobalEvent.TriggerRefresh(errorMessage = "Terjadi kesalahan dengan kode 000"), orderSummaryPageViewModel.globalEvent.value)
    }
}