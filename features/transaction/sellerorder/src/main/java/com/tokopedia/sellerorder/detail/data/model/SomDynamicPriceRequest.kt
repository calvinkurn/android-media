package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomDynamicPriceRequest(
        @Expose
        @SerializedName("order_id")
        val order_id: Long = 0,
        @Expose
        @SerializedName("lang")
        val lang: String = "id",
        @Expose
        @SerializedName("device")
        val device: String = "android"
)