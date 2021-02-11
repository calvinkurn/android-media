package com.tokopedia.sellerorder.list.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListGetTickerParam(
        @SerializedName("request_by")
        @Expose
        val requestBy: String = "seller",
        @SerializedName("client")
        @Expose
        val client: String = "android",
        @SerializedName("user_id")
        @Expose
        val userId: String = "0"
)