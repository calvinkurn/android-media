package com.tokopedia.sellerorder.list.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListBulkAcceptOrderParam(
        @SerializedName("order_id")
        @Expose
        val orderIds: String = "",
        @SerializedName("user_id")
        @Expose
        val userId: String = "0"
)