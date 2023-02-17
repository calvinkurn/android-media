package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class Tab(

    @SerializedName("isActive")
    var isActive: Boolean? = null,
    @SerializedName("items")
    var items: ArrayList<Items> = arrayListOf(),
    @SerializedName("meta")
    var meta: MetaData? = MetaData(),
    @SerializedName("__typename")
    var _typename: String? = null

)
data class MetaData(
    @SerializedName("selectedIndex")
    var selectedIndex: Int? = null,
    @SerializedName("__typename")
    var _typename: String? = null

)
