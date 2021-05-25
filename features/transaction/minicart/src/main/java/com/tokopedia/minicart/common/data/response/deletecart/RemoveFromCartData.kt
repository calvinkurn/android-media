package com.tokopedia.minicart.common.data.response.deletecart

import com.google.gson.annotations.SerializedName

data class RemoveFromCartData(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("data")
        val data: Data = Data()
)
