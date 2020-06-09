package com.tokopedia.one.click.checkout.order.data

import com.google.gson.annotations.SerializedName

data class GetOccCartResponse(
        @SerializedName("error_message")
        val errorMessages: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: GetOccCartData = GetOccCartData()
)