package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class History(
        @SerializedName("order_status_code")
        @Expose
        var orderStatusCode: Int = 0,

        @SerializedName("order_status_color")
        @Expose
        var orderStatusColor: String = "",

        @SerializedName("action_by")
        @Expose
        var actionBy: String = "",

        @SerializedName("date")
        @Expose
        var date: String = "",

        @SerializedName("hour")
        @Expose
        var hour: String = "",

        @SerializedName("comment")
        @Expose
        var comment: String = "",

        @SerializedName("status")
        @Expose
        var status: String = ""
)