package com.tokopedia.saldodetails.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.saldodetails.domain.GetWithdrawalInfoUseCase
import com.tokopedia.saldodetails.domain.usecase.GetAllTypeTransactionUseCase
import com.tokopedia.saldodetails.domain.usecase.GetSalesTransactionListUseCase
import com.tokopedia.saldodetails.domain.usecase.GetTypeTransactionsUseCase
import com.tokopedia.saldodetails.response.model.DepositActivityResponse
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse
import com.tokopedia.saldodetails.view.fragment.new.AllTransaction
import com.tokopedia.saldodetails.view.fragment.new.IncomeTransaction
import com.tokopedia.saldodetails.view.fragment.new.RefundTransaction
import com.tokopedia.saldodetails.view.viewmodel.state.InitialLoadingError
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class TransactionHistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getAllTypeTransactionUseCase = mockk<GetAllTypeTransactionUseCase>(relaxed = true)
    private val getTypeTransactionsUseCase = mockk<GetTypeTransactionsUseCase>(relaxed = true)
    private val getSalesTransactionListUseCase = mockk<GetSalesTransactionListUseCase>(relaxed = true)
    private lateinit var viewModel: TransactionHistoryViewModel
    private val dispatcher = TestCoroutineDispatcher()
    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = TransactionHistoryViewModel(getAllTypeTransactionUseCase, getTypeTransactionsUseCase, getSalesTransactionListUseCase, dispatcher)
    }

    @Test
    fun `Execute refreshAllTabsData initial load failure`() {
        coEvery { getAllTypeTransactionUseCase.loadAllTypeTransactions(any(), any(), any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.refreshAllTabsData(Date(), Date())

        assert(viewModel.getLiveDataByTransactionType(AllTransaction).value is InitialLoadingError)
        assert(viewModel.getLiveDataByTransactionType(IncomeTransaction).value is InitialLoadingError)
        assert(viewModel.getLiveDataByTransactionType(RefundTransaction).value is InitialLoadingError)

    }

    @Test
    fun `Execute refreshAllTabsData response isHavingError`() {
        val data = mockk<GqlAllDepositSummaryResponse>()
        every { data.isHavingError() } returns true
        every { data.getErrorMessage()} returns "ERROR"

        coEvery { getAllTypeTransactionUseCase.loadAllTypeTransactions(any(), any(), any(), any()) } coAnswers {
            firstArg<(GqlAllDepositSummaryResponse) -> Unit>().invoke(data)
        }
        viewModel.refreshAllTabsData(Date(), Date())

        assert(viewModel.getLiveDataByTransactionType(AllTransaction).value is InitialLoadingError)
        assert(viewModel.getLiveDataByTransactionType(IncomeTransaction).value is InitialLoadingError)
        assert(viewModel.getLiveDataByTransactionType(RefundTransaction).value is InitialLoadingError)

    }

    @Test
    fun `Execute refreshAllTabsData response ok`() {
        val data = mockk<GqlAllDepositSummaryResponse>()
        every { data.isHavingError() } returns false
        every { data.allDepositHistory } returns DepositActivityResponse("", isHaveError = false, false, listOf())
        every { data.allDepositHistory } returns DepositActivityResponse("", isHaveError = false, false, listOf())

        coEvery { getAllTypeTransactionUseCase.loadAllTypeTransactions(any(), any(), any(), any()) } coAnswers {
            firstArg<(GqlAllDepositSummaryResponse) -> Unit>().invoke(data)
        }
        viewModel.refreshAllTabsData(Date(), Date())

        assert(viewModel.getLiveDataByTransactionType(AllTransaction).value is InitialLoadingError)
        assert(viewModel.getLiveDataByTransactionType(IncomeTransaction).value is InitialLoadingError)
        assert(viewModel.getLiveDataByTransactionType(RefundTransaction).value is InitialLoadingError)

    }
}