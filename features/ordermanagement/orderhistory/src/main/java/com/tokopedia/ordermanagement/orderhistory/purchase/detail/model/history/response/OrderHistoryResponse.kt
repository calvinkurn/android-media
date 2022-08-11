package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.OrderDetailResponseError

data class OrderHistoryResponse(
    @SerializedName("get_buyer_history")
    val getBuyerHistory: GetBuyerHistory = GetBuyerHistory()
)

data class GetBuyerHistory(
    @SerializedName("data")
    val buyerHistoryData: BuyerHistoryData = BuyerHistoryData(),
    @SerializedName("errors")
    val errorList: List<OrderDetailResponseError> = emptyList()
)