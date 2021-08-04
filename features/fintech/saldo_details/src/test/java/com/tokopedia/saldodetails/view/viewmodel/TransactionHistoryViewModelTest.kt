package com.tokopedia.saldodetails.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.saldodetails.domain.GetWithdrawalInfoUseCase
import com.tokopedia.saldodetails.domain.model.GQLSalesTransactionListResponse
import com.tokopedia.saldodetails.domain.model.SalesTransactionListResponse
import com.tokopedia.saldodetails.domain.usecase.GetAllTypeTransactionUseCase
import com.tokopedia.saldodetails.domain.usecase.GetSalesTransactionListUseCase
import com.tokopedia.saldodetails.domain.usecase.GetTypeTransactionsUseCase
import com.tokopedia.saldodetails.response.model.DepositActivityResponse
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse
import com.tokopedia.saldodetails.response.model.GqlCompleteTransactionResponse
import com.tokopedia.saldodetails.view.fragment.new.AllTransaction
import com.tokopedia.saldodetails.view.fragment.new.IncomeTransaction
import com.tokopedia.saldodetails.view.fragment.new.RefundTransaction
import com.tokopedia.saldodetails.view.fragment.new.SalesTransaction
import com.tokopedia.saldodetails.view.viewmodel.state.InitialLoadingError
import com.tokopedia.saldodetails.view.viewmodel.state.LoadMoreError
import com.tokopedia.saldodetails.view.viewmodel.state.SaldoHistoryResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
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
        every { data.buyerDepositHistory } returns DepositActivityResponse("", isHaveError = false, false, listOf())
        every { data.sellerDepositHistory } returns DepositActivityResponse("", isHaveError = false, false, listOf())

        coEvery { getAllTypeTransactionUseCase.loadAllTypeTransactions(any(), any(), any(), any()) } coAnswers {
            firstArg<(GqlAllDepositSummaryResponse) -> Unit>().invoke(data)
        }
        viewModel.refreshAllTabsData(Date(), Date())

        assert(viewModel.getLiveDataByTransactionType(AllTransaction).value is SaldoHistoryResponse)
        assert(viewModel.getLiveDataByTransactionType(IncomeTransaction).value is SaldoHistoryResponse)
        assert(viewModel.getLiveDataByTransactionType(RefundTransaction).value is SaldoHistoryResponse)

    }

    @Test
    fun `Execute loadMoreTransaction type - SalesTransaction expect InitialLoadingError`() {
        coEvery { getSalesTransactionListUseCase.loadSalesTransactions(any(), any(), any(), any(), any()) } coAnswers {
            secondArg<(Throwable) ->Unit>().invoke(mockThrowable)
        }
        viewModel.loadMoreTransaction(SalesTransaction)
        assert(viewModel.getLiveDataByTransactionType(SalesTransaction).value is InitialLoadingError)
    }

    @Test
    fun `Execute loadMoreTransaction {type - SalesTransaction} Initial Load{Fail}`() {
        getSalesFailData()
        assert(viewModel.getLiveDataByTransactionType(SalesTransaction).value is InitialLoadingError)
    }

    @Test
    fun `Execute loadMoreTransaction type - SalesTransaction messageStatus {Fail} load more`() {
        // page1 hit
        getSalesSuccessData()
        assert(viewModel.getLiveDataByTransactionType(SalesTransaction).value is SaldoHistoryResponse)

        // page 2 load more hit
        getSalesFailData()
        assert(viewModel.getLiveDataByTransactionType(SalesTransaction).value is LoadMoreError)
    }

    @Test
    fun `Execute loadMoreTransaction type - SalesTransaction messageStatus {Success}`() {
        getSalesSuccessData()
        assert(viewModel.getLiveDataByTransactionType(SalesTransaction).value is SaldoHistoryResponse)
    }

    private fun getSalesSuccessData() {
        val data = mockk<GQLSalesTransactionListResponse>()
        val res = SalesTransactionListResponse("Success", "", "", 1, true, listOf())
        every { data.salesTransactionListResponse } returns res

        coEvery { getSalesTransactionListUseCase.loadSalesTransactions(any(), any(), any(), any(), any()) } coAnswers {
            firstArg<(GQLSalesTransactionListResponse) ->Unit>().invoke(data)
        }
        viewModel.loadMoreTransaction(SalesTransaction)
    }

    private fun getSalesFailData() {
        val data = mockk<GQLSalesTransactionListResponse>()
        val res = SalesTransactionListResponse("Fail", "desc", "errorTitle", 1, false, listOf())
        every { data.salesTransactionListResponse } returns res

        coEvery { getSalesTransactionListUseCase.loadSalesTransactions(any(), any(), any(), any(), any()) } coAnswers {
            firstArg<(GQLSalesTransactionListResponse) ->Unit>().invoke(data)
        }
        viewModel.loadMoreTransaction(SalesTransaction)
    }


    @Test
    fun `Execute loadMoreTransaction load more error`() {
        coEvery { getTypeTransactionsUseCase.loadTypeTransactions(any(), any(), any(), any(), any(), any()) } coAnswers {
            secondArg<(Throwable) ->Unit>().invoke(mockThrowable)
        }

        viewModel.loadMoreTransaction(AllTransaction)
        assert(viewModel.getLiveDataByTransactionType(AllTransaction).value is LoadMoreError)

        viewModel.loadMoreTransaction(RefundTransaction)
        assert(viewModel.getLiveDataByTransactionType(RefundTransaction).value is LoadMoreError)

        viewModel.loadMoreTransaction(IncomeTransaction)
        assert(viewModel.getLiveDataByTransactionType(IncomeTransaction).value is LoadMoreError)

    }

    @Test
    fun `Execute loadMoreTransaction notifyAndAddLoadMoreTransaction isHaveError = true`() {
        val data = mockk<GqlCompleteTransactionResponse>()
        every { data.allDepositHistory?.isHaveError } returns true
        every { data.allDepositHistory?.message } returns "Error"

        coEvery { getTypeTransactionsUseCase.loadTypeTransactions(any(), any(), any(), any(), any(), any()) } coAnswers {
            firstArg<(GqlCompleteTransactionResponse) ->Unit>().invoke(data)
        }

        viewModel.loadMoreTransaction(AllTransaction)
        assert(viewModel.getLiveDataByTransactionType(AllTransaction).value is LoadMoreError)

        viewModel.loadMoreTransaction( RefundTransaction)
        assert(viewModel.getLiveDataByTransactionType(RefundTransaction).value is LoadMoreError)

        viewModel.loadMoreTransaction(IncomeTransaction)
        assert(viewModel.getLiveDataByTransactionType(IncomeTransaction).value is LoadMoreError)

    }

    @Test
    fun `Execute loadMoreTransaction notifyAndAddLoadMoreTransaction`() {
         val data = mockk<GqlCompleteTransactionResponse>()
        every { data.allDepositHistory?.isHaveNextPage } returns true
        every { data.allDepositHistory?.isHaveError } returns false
        every { data.allDepositHistory?.depositHistoryList } returns listOf()

        coEvery { getTypeTransactionsUseCase.loadTypeTransactions(any(), any(), any(), any(), any(), any()) } coAnswers {
            firstArg<(GqlCompleteTransactionResponse) ->Unit>().invoke(data)
        }

        viewModel.loadMoreTransaction(AllTransaction)
        assert(viewModel.getLiveDataByTransactionType(AllTransaction).value is SaldoHistoryResponse)

        viewModel.loadMoreTransaction(RefundTransaction)
        assert(viewModel.getLiveDataByTransactionType(RefundTransaction).value is SaldoHistoryResponse)

        viewModel.loadMoreTransaction(IncomeTransaction)
        assert(viewModel.getLiveDataByTransactionType(IncomeTransaction).value is SaldoHistoryResponse)

    }

}