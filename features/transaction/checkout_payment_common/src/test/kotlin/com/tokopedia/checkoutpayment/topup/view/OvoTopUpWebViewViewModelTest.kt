package com.tokopedia.checkoutpayment.topup.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.checkoutpayment.list.view.Failure
import com.tokopedia.checkoutpayment.list.view.OccState
import com.tokopedia.checkoutpayment.topup.data.PaymentCustomerData
import com.tokopedia.checkoutpayment.topup.domain.GetOvoTopUpUrlUseCase
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
        viewModel.getOvoTopUpUrl("", PaymentCustomerData())

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
        viewModel.getOvoTopUpUrl("", PaymentCustomerData())

        // then
        assertEquals(OccState.Failed(Failure(throwable)), viewModel.ovoTopUpUrl.value)
    }
}
