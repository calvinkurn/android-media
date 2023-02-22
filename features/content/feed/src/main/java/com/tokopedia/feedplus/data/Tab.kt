package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class Tab(

    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("items")
    val items: ArrayList<Items> = arrayListOf(),
    @SerializedName("meta")
    val meta: MetaData? = MetaData(),
    @SerializedName("__typename")
    val _typename: String = ""

)
data class MetaData(
    @SerializedName("selectedIndex")
    val selectedIndex: Int = 0,
    @SerializedName("__typename")
    val _typename: String = ""

)
