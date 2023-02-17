package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class Items(

    @SerializedName("isActive")
    var isActive: Boolean? = null,
    @SerializedName("position")
    var position: Int? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("key")
    var key: String? = null,
    @SerializedName("__typename")
    var _typename: String? = null

)
