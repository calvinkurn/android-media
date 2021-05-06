package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("order_id")
        @Expose
        var orderId: Long = 0,

        @SerializedName("order_status_code")
        @Expose
        var orderStatusCode: Int = 0,

        @SerializedName("order_status_color")
        @Expose
        var orderStatusColor: String = "",

        @SerializedName("order_status")
        @Expose
        var orderStatus: String = "",

        @SerializedName("history_img")
        @Expose
        var historyImg: String = "",

        @SerializedName("history_title")
        @Expose
        var historyTitle: String = "",

        @SerializedName("histories")
        @Expose
        var histories: List<History> = emptyList()
)