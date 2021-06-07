package com.tokopedia.oneclickcheckout.preference.edit.view.payment.topup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentOvoCustomerData
import com.tokopedia.oneclickcheckout.payment.topup.domain.GetOvoTopUpUrlUseCase
import com.tokopedia.oneclickcheckout.payment.topup.view.OvoTopUpWebViewViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OvoTopUpWebViewViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val getOvoTopUpUrlUseCase: GetOvoTopUpUrlUseCase = mockk()

    private lateinit var viewModel: OvoTopUpWebViewViewModel

    @Before
    fun setup() {
        viewModel = OvoTopUpWebViewViewModel(getOvoTopUpUrlUseCase)
    }

    @Test
    fun `GetOvoTopUpUrl Success`() {
        // given
        val url = "url"
        every {
            getOvoTopUpUrlUseCase.execute(any(), any(), any(), any(), any(), any())
        } answers {
            (args[4] as ((String) -> Unit)).invoke(url)
        }

        // when
        viewModel.getOvoTopUpUrl("", OrderPaymentOvoCustomerData())

        // then
        assertEquals(OccState.Success(url), viewModel.ovoTopUpUrl.value)
    }

    @Test
    fun `GetOvoTopUpUrl Failed`() {
        // given
        val throwable = Throwable()
        every {
            getOvoTopUpUrlUseCase.execute(any(), any(), any(), any(), any(), any())
        } answers {
            (args[5] as ((Throwable) -> Unit)).invoke(throwable)
        }

        // when
        viewModel.getOvoTopUpUrl("", OrderPaymentOvoCustomerData())

        // then
        assertEquals(OccState.Failed(Failure(throwable)), viewModel.ovoTopUpUrl.value)
    }
}