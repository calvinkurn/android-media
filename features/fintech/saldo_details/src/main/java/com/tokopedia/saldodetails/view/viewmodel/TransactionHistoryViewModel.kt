package com.tokopedia.saldodetails.view.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.saldodetails.di.DispatcherModule
import com.tokopedia.saldodetails.domain.model.SalesTransactionDetail
import com.tokopedia.saldodetails.domain.model.SalesTransactionListResponse
import com.tokopedia.saldodetails.domain.usecase.GetAllTypeTransactionUseCase
import com.tokopedia.saldodetails.domain.usecase.GetSalesTransactionListUseCase
import com.tokopedia.saldodetails.domain.usecase.GetTypeTransactionsUseCase
import com.tokopedia.saldodetails.response.model.DepositHistoryList
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse
import com.tokopedia.saldodetails.response.model.GqlCompleteTransactionResponse
import com.tokopedia.saldodetails.view.fragment.new.*
import com.tokopedia.saldodetails.view.viewmodel.state.*
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


    private var startDate: Date = Date()
    private var endDate: Date = Date()

    private val allTransactionList = arrayListOf<DepositHistoryList>()
    private val refundTransactionList = arrayListOf<DepositHistoryList>()
    private val incomeTransactionList = arrayListOf<DepositHistoryList>()
    private val salesTransactionList = arrayListOf<SalesTransactionDetail>()

    private val allTransactionLiveData =
        MutableLiveData<SaldoResponse>() //for All saldo transaction
    private val refundTransactionLiveData =
        MutableLiveData<SaldoResponse>() //for Refund transaction
    private val incomeTransactionLiveData = MutableLiveData<SaldoResponse>() //for Saldo Penghasilan
    private val salesTransactionLiveData = MutableLiveData<SaldoResponse>() //for Saldo Penjualan

    fun getLiveDataByTransactionType(transactionType: TransactionType): LiveData<SaldoResponse> {
        return when (transactionType) {
            AllTransaction -> allTransactionLiveData
            IncomeTransaction -> incomeTransactionLiveData
            RefundTransaction -> refundTransactionLiveData
            SalesTransaction -> salesTransactionLiveData
        }
    }

    fun refreshAllTabsData(startDate: Date, endDate: Date) {
        this.startDate = startDate
        this.endDate = endDate
        clearPrevData()
        cancelTransactionLoading()
        allTransactionLiveData.postValue(InitialLoadingState)
        refundTransactionLiveData.postValue(InitialLoadingState)
        salesTransactionLiveData.postValue(InitialLoadingState)
        incomeTransactionLiveData.postValue(InitialLoadingState)
        getAllTypeTransactionUseCase.loadAllTypeTransactions(
            {
                onAllTabDataLoaded(it)
            }, {
                onAllTabsDataError(it)
            }, startDate, endDate
        )
        loadSaleTransaction(1)

    }

    private fun clearPrevData() {
        allTransactionList.clear()
        refundTransactionList.clear()
        salesTransactionList.clear()
        incomeTransactionList.clear()
    }

    private fun loadSaleTransaction(page: Int) {
        getSalesTransactionListUseCase.loadSalesTransactions(page, startDate, endDate, {
            onSalesTabDataLoaded(it.salesTransactionListResponse, page)
        }, {
            if (page == 1) {
                salesTransactionLiveData
                    .postValue(InitialLoadingError(it))
            } else {

                salesTransactionLiveData
                    .postValue(LoadMoreError(it))
            }
        })
    }

    private fun onSalesTabDataLoaded(response: SalesTransactionListResponse, page: Int) {
        if (response.messageStatus == "Success") {
            response.transactionList.let {
                salesTransactionList.addAll(it)
                salesTransactionLiveData.postValue(
                    SaldoHistoryResponse(
                        salesTransactionList,
                        response.isHaveNextPage
                    )
                )
            }
        } else {
            if (page == 1) {
                salesTransactionLiveData
                    .postValue(InitialLoadingError(Exception(response.publicMessageTitle)))
            } else {

                salesTransactionLiveData
                    .postValue(LoadMoreError(Exception(response.publicMessageTitle)))
            }
        }
    }

    private fun onAllTabDataLoaded(response: GqlAllDepositSummaryResponse) {
        if (response.isHavingError()) {
            onAllTabsDataError(Exception(response.getErrorMessage()))
        } else {
            response.allDepositHistory?.let {
                allTransactionList.addAll(it.depositHistoryList ?: mutableListOf())
                allTransactionLiveData.postValue(
                    SaldoHistoryResponse(
                        allTransactionList,
                        it.isHaveNextPage
                    )
                )
            }
            response.buyerDepositHistory?.let {
                refundTransactionList.addAll(it.depositHistoryList ?: mutableListOf())
                refundTransactionLiveData.postValue(
                    SaldoHistoryResponse(
                        refundTransactionList,
                        it.isHaveNextPage
                    )
                )
            }
            response.sellerDepositHistory?.let {
                incomeTransactionList.addAll(it.depositHistoryList ?: mutableListOf())
                incomeTransactionLiveData.postValue(
                    SaldoHistoryResponse(
                        incomeTransactionList,
                        it.isHaveNextPage
                    )
                )
            }
        }
    }

    private fun onAllTabsDataError(throwable: Throwable) {
        allTransactionLiveData
            .postValue(InitialLoadingError(throwable))
        refundTransactionLiveData
            .postValue(InitialLoadingError(throwable))
        incomeTransactionLiveData
            .postValue(InitialLoadingError(throwable))
    }

    fun loadMoreTransaction(page: Int, transactionType: TransactionType) {
        updateLoadMoreState(transactionType)
        if (transactionType == SalesTransaction) {
            loadSaleTransaction(page)
        } else {
            getTypeTransactionsUseCase.loadTypeTransactions(page,
                startDate, endDate, transactionType, {
                    notifyAndAddLoadMoreTransaction(it, transactionType)
                }, {
                    notifyLoadMoreError(it, transactionType)
                }
            )
        }
    }

    private fun updateLoadMoreState(transactionType: TransactionType) {
        when (transactionType) {
            AllTransaction -> allTransactionLiveData.postValue(LoadingMoreState)
            RefundTransaction -> refundTransactionLiveData.postValue(LoadingMoreState)
            IncomeTransaction -> incomeTransactionLiveData.postValue(LoadingMoreState)
            SalesTransaction -> salesTransactionLiveData.postValue(LoadingMoreState)
        }
    }

    private fun notifyAndAddLoadMoreTransaction(
        it: GqlCompleteTransactionResponse,
        transactionType: TransactionType
    ) {
        if (it.allDepositHistory?.isHaveError == true) {
            notifyLoadMoreError(
                Exception(it.allDepositHistory?.message ?: ""),
                transactionType
            )
        } else
            when (transactionType) {
                AllTransaction -> {
                    allTransactionList.addAll(
                        it.allDepositHistory?.depositHistoryList
                            ?: mutableListOf()
                    )
                    allTransactionLiveData.postValue(
                        SaldoHistoryResponse(
                            allTransactionList,
                            it.allDepositHistory?.isHaveNextPage ?: false
                        )
                    )
                }
                RefundTransaction -> {
                    refundTransactionList.addAll(
                        it.allDepositHistory?.depositHistoryList
                            ?: mutableListOf()
                    )
                    refundTransactionLiveData.postValue(
                        SaldoHistoryResponse(
                            refundTransactionList,
                            it.allDepositHistory?.isHaveNextPage ?: false
                        )
                    )
                }
                IncomeTransaction -> {
                    incomeTransactionList.addAll(
                        it.allDepositHistory?.depositHistoryList
                            ?: mutableListOf()
                    )
                    incomeTransactionLiveData.postValue(
                        SaldoHistoryResponse(
                            incomeTransactionList,
                            it.allDepositHistory?.isHaveNextPage ?: false
                        )
                    )
                }
            }
    }

    private fun notifyLoadMoreError(throwable: Throwable, transactionType: TransactionType) {
        when (transactionType) {
            AllTransaction -> {
                allTransactionLiveData.postValue(LoadMoreError(throwable))
            }
            RefundTransaction -> {
                refundTransactionLiveData.postValue(LoadMoreError(throwable))
            }
            IncomeTransaction -> {
                incomeTransactionLiveData.postValue(LoadMoreError(throwable))
            }
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

}