package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class Creation(
    @SerializedName("isActive")
    var isActive: Boolean? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("authors")
    var authors: ArrayList<Authors> = arrayListOf(),
    @SerializedName("__typename")
    var _typename: String? = null
)
