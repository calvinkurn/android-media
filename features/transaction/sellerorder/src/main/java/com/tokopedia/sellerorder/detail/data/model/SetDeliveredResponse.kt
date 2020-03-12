package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.SerializedName


data class SetDeliveredResponse(
        @SerializedName("set_delivered")
        var setDelivered: SetDelivered = SetDelivered()
)

data class SetDelivered(
        @SerializedName("message")
        var message: List<String> = listOf(),
        @SerializedName("success")
        var success: Int = 0
)