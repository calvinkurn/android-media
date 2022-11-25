package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("data")
    val data: List<HeaderData> = listOf()
)
