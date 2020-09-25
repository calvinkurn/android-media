package com.tokopedia.oneclickcheckout.preference.edit.view.payment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.ListingParam
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.PaymentListingParamRequest
import com.tokopedia.oneclickcheckout.preference.edit.domain.payment.FakeGetPaymentListingParamUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PaymentMethodViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase: FakeGetPaymentListingParamUseCase = FakeGetPaymentListingParamUseCase()

    private lateinit var viewModel: PaymentMethodViewModel

    @Before
    fun setup() {
        viewModel = PaymentMethodViewModel(useCase)
    }

    @Test
    fun `Get Payment Listing Param Success`() {
        viewModel.getPaymentListingParam(PaymentListingParamRequest("","","","",""))

        assertEquals(OccState.Loading, viewModel.paymentListingParam.value)

        val param = ListingParam()
        useCase.invokeOnSuccess(param)

        assertEquals(OccState.Success(param), viewModel.paymentListingParam.value)
    }

    @Test
    fun `Get Payment Listing Param Failed`() {
        viewModel.getPaymentListingParam(PaymentListingParamRequest("","","","",""))

        assertEquals(OccState.Loading, viewModel.paymentListingParam.value)

        val throwable = Throwable()
        useCase.invokeOnError(throwable)

        assertEquals(OccState.Failed(Failure(throwable)), viewModel.paymentListingParam.value)
    }
}