package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class History(
        @SerializedName("orderStatus")
        var orderStatusCode: Int = 0,

        @SerializedName("orderStatusColor")
        var orderStatusColor: String = "",

        @SerializedName("actionBy")
        var actionBy: String = "",

        @SerializedName("date")
        var date: String = "",

        @SerializedName("hour")
        var hour: String = "",

        @SerializedName("comment")
        var comment: String = "",

        @SerializedName("status")
        var status: String = ""
)