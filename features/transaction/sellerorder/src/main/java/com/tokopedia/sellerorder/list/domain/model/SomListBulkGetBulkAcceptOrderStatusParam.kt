package com.tokopedia.sellerorder.list.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListBulkGetBulkAcceptOrderStatusParam(
        @SerializedName("batch_id")
        @Expose
        val batchId: String = "",
        @SerializedName("shop_id")
        @Expose
        val shopId: String = ""
)