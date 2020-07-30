package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.SerializedName

data class SetDeliveredRequest(
        @SerializedName("order_id")
        var orderId: String,
        @SerializedName("received_by")
        var receivedBy: String,
        @SerializedName("user_id")
        var userId: String = "",
        @SerializedName("mobile")
        var mobile: String = "",
        @SerializedName("lang")
        var lang: String = ""
)