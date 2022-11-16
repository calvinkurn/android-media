package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("data")
    val data: List<HeaderData> = listOf()
)
