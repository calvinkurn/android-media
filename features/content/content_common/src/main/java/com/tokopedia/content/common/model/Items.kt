package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

data class Items(
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("position")
    val position: Int = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("key")
    val key: String = "",
    @SerializedName("__typename")
    val typeName: String = ""
)
