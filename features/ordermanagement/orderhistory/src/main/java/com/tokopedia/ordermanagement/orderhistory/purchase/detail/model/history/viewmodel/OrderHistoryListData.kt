package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel

/**
 * Created by kris on 11/8/17. Tokopedia
 */
data class OrderHistoryListData (
        var orderHistoryTitle: String = "",
        var orderHistoryTime: String = "",
        var orderHistoryDate: String = "",
        var orderHistoryComment: String = "",
        var actionBy: String = "",
        var color: String = ""
)