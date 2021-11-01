package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Ticker(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("page")
        val page: String = "",
        @SerializedName("title")
        val title: String = ""
)