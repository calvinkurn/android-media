package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.SerializedName

data class Options(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: String = ""
)
