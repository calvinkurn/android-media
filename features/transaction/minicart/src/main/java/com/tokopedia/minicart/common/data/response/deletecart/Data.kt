package com.tokopedia.minicart.common.data.response.deletecart

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("message")
        val message: List<String> = emptyList()
)