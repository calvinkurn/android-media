package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class GetOccCartResponse(
        @SerializedName("error_message")
        val errorMessages: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: GetOccCartData = GetOccCartData()
)