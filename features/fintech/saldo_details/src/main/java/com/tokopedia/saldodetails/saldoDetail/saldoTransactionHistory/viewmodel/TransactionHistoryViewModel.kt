package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.commom.di.module.DispatcherModule
import com.tokopedia.saldodetails.commom.utils.*
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.GqlAllDepositSummaryResponse
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.GqlCompleteTransactionResponse
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data.SalesTransactionListResponse
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.usecase.GetAllTypeTransactionUseCase
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.usecase.GetSalesTransactionListUseCase
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.usecase.GetTypeTransactionsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class TransactionHistoryViewModel @Inject constructor(
    private val getAllTypeTransactionUseCase: GetAllTypeTransactionUseCase,
    private val getTypeTransactionsUseCase: GetTypeTransactionsUseCase,
    private val getSalesTransactionListUseCase: GetSalesTransactionListUseCase,
    @Named(DispatcherModule.MAIN) val dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {


    private var dateFrom: Date = Date()
    private var dateTo: Date = Date()
    // default and oldSelected
    var preSelected: Int = 0
    var currentSelectedFilter: Int = 0


    private val allTransactionList = TransactionList()
    private val refundTransactionList = TransactionList()
    private val incomeTransactionList = TransactionList()
    private val salesTransactionList = TransactionList()

    val filterLiveData = MutableLiveData<TransactionType>()
    private val masterLiveData =
        MutableLiveData<SaldoResponse>() //for All saldo transaction
    private val allTransactionLiveData =
        MutableLiveData<SaldoResponse>() //for All saldo transaction
    private val refundTransactionLiveData =
        MutableLiveData<SaldoResponse>() //for Refund transaction
    private val incomeTransactionLiveData = MutableLiveData<SaldoResponse>() //for Saldo Penghasilan
    private val salesTransactionLiveData = MutableLiveData<SaldoResponse>() //for Saldo Penjualan
    private var transactionType: TransactionType = AllTransaction

    fun selectTransactionFilter(newFilterIndex: Int, transactionType: TransactionType) {
        // post saldo response on basis of trxtype
        if (currentSelectedFilter != newFilterIndex) {
            preSelected = currentSelectedFilter
            currentSelectedFilter = newFilterIndex
            filterLiveData.postValue(transactionType)
            this.transactionType = transactionType
            masterLiveData.postValue(getStoredLiveData(transactionType).value)
        }
    }

    fun getLiveDataByTransactionType(transactionType: TransactionType): LiveData<SaldoResponse> {
        return when (transactionType) {
            SalesTransaction -> salesTransactionLiveData
            else -> masterLiveData
        }
    }

    private fun getStoredLiveData(transactionType: TransactionType): LiveData<SaldoResponse> {
        return when (transactionType) {
            IncomeTransaction -> incomeTransactionLiveData
            RefundTransaction -> refundTransactionLiveData
            else -> allTransactionLiveData
        }
    }

    private fun getNextPageByTransactionType(transactionType: TransactionType) :Int {
        return when(transactionType) {
            AllTransaction -> allTransactionList
            IncomeTransaction -> incomeTransactionList
            RefundTransaction -> refundTransactionList
            SalesTransaction -> salesTransactionList
        }.getNextPage()
    }

    fun refreshAllTabsData(dateFrom: Date, dateTo: Date) {
        this.dateFrom = dateFrom
        this.dateTo = dateTo
        clearPrevData()
        cancelTransactionLoading()
        masterLiveData.postValue(InitialLoadingState)
        allTransactionLiveData.value = InitialLoadingState
        refundTransactionLiveData.value = InitialLoadingState
        incomeTransactionLiveData.value = InitialLoadingState
        salesTransactionLiveData.postValue(InitialLoadingState)
        getAllTypeTransactionUseCase.loadAllTypeTransactions(
            {
                onAllTabDataLoaded(it)
            }, {
                onAllTabsDataError(it)
            }, dateFrom, dateTo
        )
        loadSaleTransaction(1)
    }

    fun retryAllTabLoading() {
        refreshAllTabsData(dateFrom, dateTo)
    }

    private fun clearPrevData() {
        allTransactionList.clear()
        refundTransactionList.clear()
        salesTransactionList.clear()
        incomeTransactionList.clear()
    }

    private fun loadSaleTransaction(page: Int) {
        getSalesTransactionListUseCase.loadSalesTransactions({
            onSalesTabDataLoaded(it.salesTransactionListResponse, page)
        }, {
            if (page == 1) {
                salesTransactionLiveData
                    .postValue(InitialLoadingError(it))
            } else {

                salesTransactionLiveData
                    .postValue(LoadMoreError(it))
            }
        }, page, dateFrom, dateTo)
    }

    private fun onSalesTabDataLoaded(response: SalesTransactionListResponse, page: Int) {
        if (response.messageStatus == "Success") {
            response.transactionList.let {
                salesTransactionList.addAll(it)
                salesTransactionLiveData.postValue(
                    SaldoHistoryResponse(
                        salesTransactionList.getTransactionList() ,
                        response.isHaveNextPage
                    )
                )
            }
        } else {
            if (page == 1) {
                salesTransactionLiveData
                    .postValue(InitialLoadingError(MessageErrorException(response.publicMessageTitle)))
            } else {

                salesTransactionLiveData
                    .postValue(LoadMoreError(MessageErrorException(response.publicMessageTitle)))
            }
        }
    }

    private fun onAllTabDataLoaded(response: GqlAllDepositSummaryResponse) {
        if (response.isHavingError()) {
            onAllTabsDataError(MessageErrorException(response.getErrorMessage()))
        } else {
            response.allDepositHistory?.let {
                allTransactionList.addAll(it.depositHistoryList ?: mutableListOf())
                allTransactionLiveData.value =
                    SaldoHistoryResponse(
                        allTransactionList.getTransactionList(),
                        it.isHaveNextPage
                    )

            }
            response.buyerDepositHistory?.let {
                refundTransactionList.addAll(it.depositHistoryList ?: mutableListOf())
                refundTransactionLiveData.value = SaldoHistoryResponse(
                    refundTransactionList.getTransactionList(),
                    it.isHaveNextPage
                )
            }
            response.sellerDepositHistory?.let {
                incomeTransactionList.addAll(it.depositHistoryList ?: mutableListOf())
                incomeTransactionLiveData.value = SaldoHistoryResponse(
                    incomeTransactionList.getTransactionList(),
                    it.isHaveNextPage
                )
            }
        }
        masterLiveData.postValue(getStoredLiveData(transactionType).value)
    }

    private fun onAllTabsDataError(throwable: Throwable) {
        allTransactionLiveData.value = InitialLoadingError(throwable)
        refundTransactionLiveData.value = InitialLoadingError(throwable)
        incomeTransactionLiveData.value = InitialLoadingError(throwable)
    }

    fun loadMoreTransaction(transactionType: TransactionType) {
        updateLoadMoreState(transactionType)
        masterLiveData.postValue(getStoredLiveData(transactionType).value)
        if (transactionType == SalesTransaction) {
            loadSaleTransaction(getNextPageByTransactionType(transactionType))
        } else {
            getTypeTransactionsUseCase.loadTypeTransactions({
                notifyAndAddLoadMoreTransaction(it, transactionType)
                masterLiveData.postValue(getStoredLiveData(transactionType).value)
            }, {
                notifyLoadMoreError(it, transactionType)
                masterLiveData.postValue(getStoredLiveData(transactionType).value)
            },getNextPageByTransactionType(transactionType) , dateFrom, dateTo, transactionType)
        }
    }

    private fun updateLoadMoreState(transactionType: TransactionType) {
        when (transactionType) {
            AllTransaction -> allTransactionLiveData.value = LoadingMoreState
            RefundTransaction -> refundTransactionLiveData.value = LoadingMoreState
            IncomeTransaction -> incomeTransactionLiveData.value = LoadingMoreState
            SalesTransaction -> salesTransactionLiveData.postValue(LoadingMoreState)
        }
    }

    private fun notifyAndAddLoadMoreTransaction(
        it: GqlCompleteTransactionResponse,
        transactionType: TransactionType
    ) {
        if (it.allDepositHistory?.isHaveError == true) {
            notifyLoadMoreError(
                MessageErrorException(it.allDepositHistory?.message ?: ""),
                transactionType
            )
        } else
            when (transactionType) {
                AllTransaction -> {
                    allTransactionList.addAll(
                        it.allDepositHistory?.depositHistoryList
                            ?: mutableListOf()
                    )
                    allTransactionLiveData.value = SaldoHistoryResponse(
                        allTransactionList.getTransactionList(),
                        it.allDepositHistory?.isHaveNextPage ?: false
                    )
                }
                RefundTransaction -> {
                    refundTransactionList.addAll(
                        it.allDepositHistory?.depositHistoryList
                            ?: mutableListOf()
                    )
                    refundTransactionLiveData.value = SaldoHistoryResponse(
                        refundTransactionList.getTransactionList(),
                        it.allDepositHistory?.isHaveNextPage ?: false
                    )
                }
                IncomeTransaction -> {
                    incomeTransactionList.addAll(
                        it.allDepositHistory?.depositHistoryList
                            ?: mutableListOf()
                    )
                    incomeTransactionLiveData.value = SaldoHistoryResponse(
                        incomeTransactionList.getTransactionList(),
                        it.allDepositHistory?.isHaveNextPage ?: false
                    )

                }
            }
    }

    private fun notifyLoadMoreError(throwable: Throwable, transactionType: TransactionType) {
        when (transactionType) {
            AllTransaction -> allTransactionLiveData.value = LoadMoreError(throwable)
            RefundTransaction -> refundTransactionLiveData.value = LoadMoreError(throwable)
            IncomeTransaction -> incomeTransactionLiveData.value = LoadMoreError(throwable)
        }
    }

    private fun cancelTransactionLoading() {
        getAllTypeTransactionUseCase.cancelJobs()
        getTypeTransactionsUseCase.cancelJobs()
        getSalesTransactionListUseCase.cancelJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelTransactionLoading()
    }

    fun getEventLabelForTab(tabTitle: String): String {
        return when (tabTitle) {
            TransactionTitle.SALDO_REFUND -> SaldoDetailsConstants.Action.SALDO_REFUND_TAB_CLICK
            TransactionTitle.SALDO_INCOME -> SaldoDetailsConstants.Action.SALDO_PENGHASILAN_TAB_CLICK
            TransactionTitle.SALDO_SALES -> SaldoDetailsConstants.Action.SALDO_PENJUALAN_TAB_CLICK
            TransactionTitle.ALL_TRANSACTION -> SaldoDetailsConstants.Action.SALDO_SEMUA_FILTER_CLICK
            TransactionTitle.ALL_TAB -> SaldoDetailsConstants.Action.SALDO_SEMUA_TAB_CLICK
            else -> ""
        }
    }

    fun getEventLabelForDetail(detail: String): String {
        return when (detail) {
            TransactionTitle.SALDO_REFUND -> SaldoDetailsConstants.Action.SALDO_REFUND_DETAIL_CLICK
            TransactionTitle.SALDO_INCOME -> SaldoDetailsConstants.Action.SALDO_PENGHASILAN_DETAIL_CLICK
            TransactionTitle.SALDO_SALES -> SaldoDetailsConstants.Action.SALDO_SALES_DETAIL_CLICK
            else -> ""
        }
    }

    fun getEventLabelForList(transactionType: TransactionType): String {
        return if (transactionType is SalesTransaction)
            SaldoDetailsConstants.EventLabel.SALDO_FETCH_SALES_LIST
        else SaldoDetailsConstants.EventLabel.SALDO_FETCH_WITHDRAWAL_LIST
    }

}