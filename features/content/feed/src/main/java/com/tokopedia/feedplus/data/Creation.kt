package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class Creation(
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("image")
    val image: String = "",
    @SerializedName("authors")
    val authors: List<Authors> = emptyList(),
    @SerializedName("__typename")
    val _typename: String = ""
)
