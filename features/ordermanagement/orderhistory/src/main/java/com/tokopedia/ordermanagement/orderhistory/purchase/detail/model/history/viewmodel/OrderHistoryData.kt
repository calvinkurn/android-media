package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel

/**
 * Created by kris on 11/8/17. Tokopedia
 */
data class OrderHistoryData (
        var stepperMode: Int = 0,
        var stepperStatusTitle: String = "",
        var historyImage: String = "",
        var orderListData: List<OrderHistoryListData> = listOf()
)