package com.tokopedia.oneclickcheckout.order.data

import com.google.gson.annotations.SerializedName

data class UpdateCartOccResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: UpdateCartDataOcc
)