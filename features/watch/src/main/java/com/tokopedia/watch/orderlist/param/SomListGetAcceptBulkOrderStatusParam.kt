package com.tokopedia.watch.orderlist.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListGetAcceptBulkOrderStatusParam(
        @SerializedName("batch_id")
        @Expose
        val batchId: String = "",
        @SerializedName("shop_id")
        @Expose
        val shopId: String = ""
)