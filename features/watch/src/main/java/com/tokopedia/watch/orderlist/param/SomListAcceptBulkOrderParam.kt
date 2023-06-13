package com.tokopedia.watch.orderlist.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListAcceptBulkOrderParam(
        @SerializedName("order_id")
        @Expose
        val orderIds: String = "",
        @SerializedName("user_id")
        @Expose
        val userId: String = "0"
)