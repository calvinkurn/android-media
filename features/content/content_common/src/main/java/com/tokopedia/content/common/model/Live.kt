package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

data class Live(
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("weblink")
    val weblink: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("__typename")
    val typeName: String = ""
)
