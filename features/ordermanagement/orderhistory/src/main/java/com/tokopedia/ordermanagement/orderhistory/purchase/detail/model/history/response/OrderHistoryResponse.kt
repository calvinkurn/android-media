package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.OrderDetailResponseError

data class OrderHistoryResponse(
        @SerializedName("data")
        @Expose
        val data: Data = Data(),

        @SerializedName("errors")
        @Expose
        val errorList: List<OrderDetailResponseError> = emptyList()
)