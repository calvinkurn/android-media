package com.tokopedia.saldodetails.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.saldodetails.transactionDetailPages.penjualan.DepositHistoryData
import com.tokopedia.saldodetails.transactionDetailPages.penjualan.DepositHistoryInvoiceDetailViewModel
import com.tokopedia.saldodetails.transactionDetailPages.penjualan.GetDepositHistoryInfoUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DepositHistoryInvoiceDetailViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getDepositHistoryInfoUseCase = mockk<GetDepositHistoryInfoUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: DepositHistoryInvoiceDetailViewModel
    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setup() {
        viewModel = DepositHistoryInvoiceDetailViewModel(getDepositHistoryInfoUseCase, dispatcher)
    }
    
    @Test
    fun `Execute getDepositDetail (Gql Error)`() {
        coEvery { getDepositHistoryInfoUseCase.getDepositHistoryInvoiceInfo(any(), any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getInvoiceDetail("1234")
        assert(viewModel.depositHistoryLiveData.value is Fail)
    }


    @Test
    fun `Execute getDepositDetail Success`() {
        val data = DepositHistoryData(1.0, "", "INV001", "/invUrl", "/orderUrl", arrayListOf())
        coEvery { getDepositHistoryInfoUseCase.getDepositHistoryInvoiceInfo(any(), any(), any()) } coAnswers {
            firstArg<(DepositHistoryData) -> Unit>().invoke(data)
        }
        viewModel.getInvoiceDetail("1234")
        viewModel.getInvoiceDetailUrl()
        viewModel.getOrderDetailUrl()
        assert(viewModel.depositHistoryLiveData.value is Success)
        Assertions.assertThat((viewModel.depositHistoryLiveData.value as Success).data.invoiceNumber)
            .isEqualTo("INV001")
        Assertions.assertThat(viewModel.getInvoiceDetailUrl())
            .isEqualTo("/invUrl")
        Assertions.assertThat(viewModel.getOrderDetailUrl())
            .isEqualTo("/orderUrl")
    }

}