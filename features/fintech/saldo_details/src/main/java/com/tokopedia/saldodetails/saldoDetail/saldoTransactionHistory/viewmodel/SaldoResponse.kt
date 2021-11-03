package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

sealed class SaldoResponse
object InitialLoadingState : SaldoResponse()
data class InitialLoadingError(val throwable: Throwable) : SaldoResponse()
object LoadingMoreState : SaldoResponse()
data class LoadMoreError(val throwable: Throwable)  : SaldoResponse()
data class SaldoHistoryResponse(val historyList : List<Visitable<*>>,
                                val hasMore : Boolean) : SaldoResponse()


class TransactionList{
    private val list : ArrayList<Visitable<*>> = arrayListOf()
    private var nextPage : Int = 1

    fun getTransactionList() : List<Visitable<*>> = list

    fun getNextPage(): Int {
        return nextPage
    }


    fun clear(){
        list.clear()
        nextPage = 1
    }

    fun addAll(list: List<Visitable<*>>){
        this.list.addAll(list)
        nextPage++
    }
}
