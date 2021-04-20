package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model

import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData

sealed class OrderHistoryResult {
    object OrderHistoryLoading : OrderHistoryResult()
    data class OrderHistorySuccess(val response: OrderHistoryData) : OrderHistoryResult()
    data class OrderHistoryFail(val throwable: Throwable) : OrderHistoryResult()
}