package com.tokopedia.saldodetails.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.saldodetails.domain.GetAllTypeTransactionUseCase
import com.tokopedia.saldodetails.domain.GetTypeTransactionsUseCase
import com.tokopedia.saldodetails.response.model.DepositHistoryList
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse
import com.tokopedia.saldodetails.response.model.GqlCompleteTransactionResponse
import com.tokopedia.saldodetails.view.fragment.new.*
import com.tokopedia.saldodetails.view.viewmodel.state.*
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

class TransactionHistoryViewModel @Inject constructor(
        private val getAllTypeTransactionUseCase: GetAllTypeTransactionUseCase,
        private val getTypeTransactionsUseCase: GetTypeTransactionsUseCase,
) : BaseViewModel(Main) {

    private var startUnixTime: Long = 0L
    private var endUnixTime: Long = 0L

    private val allTransactionList = arrayListOf<DepositHistoryList>()
    private val refundTransactionList = arrayListOf<DepositHistoryList>()
    private val incomeTransactionList = arrayListOf<DepositHistoryList>()
    private val salesTransactionList = arrayListOf<DepositHistoryList>()

    private val allTransactionLiveData = MutableLiveData<SaldoResponse>() //for All saldo transaction
    private val refundTransactionLiveData = MutableLiveData<SaldoResponse>() //for Refund transaction
    private val incomeTransactionLiveData = MutableLiveData<SaldoResponse>() //for Saldo Penghasilan
    private val salesTransactionLiveData = MutableLiveData<SaldoResponse>() //for Saldo Penjualan

    fun getLiveDataByTransactionType(transactionType: TransactionType) : LiveData<SaldoResponse>{
        return when(transactionType){
            AllTransaction -> allTransactionLiveData
            IncomeTransaction -> incomeTransactionLiveData
            RefundTransaction -> refundTransactionLiveData
            SalesTransaction -> salesTransactionLiveData
        }
    }


    fun refreshAllTabsData(startUnixTime: Long, endUnixTime: Long) {
        this.startUnixTime = startUnixTime
        this.endUnixTime = endUnixTime
        clearPrevData()
        cancelTransactionLoading()
        allTransactionLiveData.postValue(InitialLoadingState)
        refundTransactionLiveData.postValue(InitialLoadingState)
        salesTransactionLiveData.postValue(InitialLoadingState)
        incomeTransactionLiveData.postValue(InitialLoadingState)
        getAllTypeTransactionUseCase.loadAllTypeTransactions(startUnixTime, endUnixTime,
                {
                    onAllTabDataLoaded(it)
                }, {
            onAllTabsDataError(it)
        }
        )
    }

    fun loadMoreTransaction(page: Int, transactionType: TransactionType) {
        updateLoadMoreState(transactionType)
        if (transactionType == SalesTransaction) {
            loadMoreSaleTransaction(page)
        } else {
            getTypeTransactionsUseCase.loadTypeTransactions(
                    startUnixTime, endUnixTime, transactionType, {
                notifyAndAddLoadMoreTransaction(it, transactionType)
            }, {
                notifyLoadMoreError(it, transactionType)
            }
            )
        }
    }

    private fun loadMoreSaleTransaction(page: Int) {

    }

    private fun onAllTabDataLoaded(response: GqlAllDepositSummaryResponse) {
        if (response.isHavingError()) {
            onAllTabsDataError(Exception(response.getErrorMessage()))
        } else {
            response.allDepositHistory?.let {
                allTransactionList.addAll(it.depositHistoryList ?: mutableListOf())
                allTransactionLiveData.postValue(SaldoHistoryResponse(allTransactionList,
                        it.isHaveNextPage))
            }
            response.buyerDepositHistory?.let {
                refundTransactionList.addAll(it.depositHistoryList ?: mutableListOf())
                refundTransactionLiveData.postValue(SaldoHistoryResponse(allTransactionList,
                        it.isHaveNextPage))
            }
            response.sellerDepositHistory?.let {
                incomeTransactionList.addAll(it.depositHistoryList ?: mutableListOf())
                incomeTransactionLiveData.postValue(SaldoHistoryResponse(allTransactionList,
                        it.isHaveNextPage))
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

    private fun updateLoadMoreState(transactionType: TransactionType) {
        when (transactionType) {
            AllTransaction -> allTransactionLiveData.postValue(LoadingMoreState)
            RefundTransaction -> allTransactionLiveData.postValue(LoadingMoreState)
            IncomeTransaction -> allTransactionLiveData.postValue(LoadingMoreState)
            SalesTransaction -> allTransactionLiveData.postValue(LoadingMoreState)
        }
    }

    private fun notifyAndAddLoadMoreTransaction(it: GqlCompleteTransactionResponse,
                                                transactionType: TransactionType) {
        if (it.allDepositHistory?.isHaveError == true) {
            notifyLoadMoreError(Exception(it.allDepositHistory?.message ?: ""),
                    transactionType)
        } else
            when (transactionType) {
                AllTransaction -> {
                    allTransactionList.addAll(it.allDepositHistory?.depositHistoryList
                            ?: mutableListOf())
                    allTransactionLiveData.postValue(SaldoHistoryResponse(allTransactionList,
                            it.allDepositHistory?.isHaveNextPage ?: false))
                }
                RefundTransaction -> {
                    refundTransactionList.addAll(it.allDepositHistory?.depositHistoryList
                            ?: mutableListOf())
                    refundTransactionLiveData.postValue(SaldoHistoryResponse(refundTransactionList,
                            it.allDepositHistory?.isHaveNextPage ?: false))
                }
                IncomeTransaction -> {
                    incomeTransactionList.addAll(it.allDepositHistory?.depositHistoryList
                            ?: mutableListOf())
                    incomeTransactionLiveData.postValue(SaldoHistoryResponse(incomeTransactionList,
                            it.allDepositHistory?.isHaveNextPage ?: false))
                }
                SalesTransaction -> TODO()
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
            SalesTransaction -> {
                salesTransactionLiveData.postValue(LoadMoreError(throwable))
            }
        }
    }

    private fun clearPrevData() {
        allTransactionList.clear()
        refundTransactionList.clear()
        salesTransactionList.clear()
        incomeTransactionList.clear()
    }

    private fun cancelTransactionLoading() {
        getAllTypeTransactionUseCase.cancelJobs()
        getTypeTransactionsUseCase.cancelJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelTransactionLoading()
    }

}