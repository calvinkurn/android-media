package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class UpdateCartDataOcc(
        @SerializedName("messages")
        val messages: List<String> = emptyList(),
        @SerializedName("success")
        val success: Int = 0
)