package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class GetOccCartResponse(
        @SerializedName("error_message")
        val errorMessages: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: GetOccCartData = GetOccCartData()
)