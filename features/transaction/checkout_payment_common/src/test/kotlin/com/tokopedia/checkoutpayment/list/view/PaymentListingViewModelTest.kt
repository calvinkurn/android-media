package com.tokopedia.checkoutpayment.list.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.checkoutpayment.list.data.ListingParam
import com.tokopedia.checkoutpayment.list.data.PaymentListingParamRequest
import com.tokopedia.checkoutpayment.list.domain.FakeGetPaymentListingParamUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PaymentListingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase: FakeGetPaymentListingParamUseCase = FakeGetPaymentListingParamUseCase()

    private lateinit var viewModel: PaymentListingViewModel

    @Before
    fun setup() {
        viewModel = PaymentListingViewModel(useCase)
    }

    @Test
    fun `Get Payment Listing Param Success`() {
        viewModel.getPaymentListingPayload(PaymentListingParamRequest("", "", "", "", "", "", ""), 0.0, "", "")

        assertEquals(OccState.Loading, viewModel.paymentListingPayload.value)

        val param = ListingParam()
        useCase.invokeOnSuccess(param)

        val result = "merchant_code=&" +
            "profile_code=&" +
            "user_id=&" +
            "customer_name=&" +
            "customer_email=&" +
            "customer_msisdn=&" +
            "address_id=&" +
            "callback_url=&" +
            "version=&" +
            "signature=&" +
            "amount=0.0&" +
            "bid=&" +
            "order_metadata=&" +
            "promo_param=&" +
            "unique_key="
        assertEquals(OccState.Success(result), viewModel.paymentListingPayload.value)
    }

    @Test
    fun `Get Payment Listing Param Failed`() {
        viewModel.getPaymentListingPayload(PaymentListingParamRequest("", "", "", "", "", "", ""), 0.0, "", "")

        assertEquals(OccState.Loading, viewModel.paymentListingPayload.value)

        val throwable = Throwable()
        useCase.invokeOnError(throwable)

        assertEquals(OccState.Failed(Failure(throwable)), viewModel.paymentListingPayload.value)
    }
}
