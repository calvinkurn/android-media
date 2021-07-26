package com.tokopedia.saldodetails.view.viewmodel.state

import com.tokopedia.saldodetails.response.model.DepositHistoryList

sealed class SaldoResponse
object InitialLoadingState : SaldoResponse()
data class InitialLoadingError(val throwable: Throwable) : SaldoResponse()
object LoadingMoreState : SaldoResponse()
data class LoadMoreError(val throwable: Throwable)  : SaldoResponse()
data class SaldoHistoryResponse(val historyList : ArrayList<DepositHistoryList>,
                                val hasMore : Boolean) : SaldoResponse()
