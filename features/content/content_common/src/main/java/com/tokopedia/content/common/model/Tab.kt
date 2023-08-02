package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

data class Tab(
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("items")
    val items: List<Items> = emptyList(),
    @SerializedName("meta")
    val meta: MetaData? = MetaData(),
    @SerializedName("__typename")
    val typeName: String = ""
)
data class MetaData(
    @SerializedName("selectedIndex")
    val selectedIndex: Int = 0,
    @SerializedName("__typename")
    val typeName: String = ""
)
