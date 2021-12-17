package com.tokopedia.attachproduct.data.model

import com.google.gson.annotations.SerializedName

data class FreeOngkir (
    @SerializedName("isActive")
    val isActive: Boolean = false,

    @SerializedName("imgURL")
    val imageUrl: String = ""
)