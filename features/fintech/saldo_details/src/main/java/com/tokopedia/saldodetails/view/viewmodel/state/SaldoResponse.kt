package com.tokopedia.saldodetails.view.viewmodel.state

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.saldodetails.response.model.DepositHistoryList

sealed class SaldoResponse
object InitialLoadingState : SaldoResponse()
data class InitialLoadingError(val throwable: Throwable) : SaldoResponse()
object LoadingMoreState : SaldoResponse()
data class LoadMoreError(val throwable: Throwable)  : SaldoResponse()
data class SaldoHistoryResponse(val historyList : List<Visitable<*>>,
                                val hasMore : Boolean) : SaldoResponse()
