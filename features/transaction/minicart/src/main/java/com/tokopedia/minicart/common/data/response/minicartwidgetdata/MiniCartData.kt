package com.tokopedia.minicart.common.data.response.minicartwidgetdata

import com.google.gson.annotations.SerializedName

data class MiniCartData(
        @SerializedName("data")
        val miniCartData: Data = Data(),
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = ""
)