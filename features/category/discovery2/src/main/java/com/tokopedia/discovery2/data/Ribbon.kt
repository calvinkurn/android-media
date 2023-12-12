package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class Ribbon(
    @SerializedName("background_color")
    val backgroundColor: String? = null,

    @SerializedName("font_color")
    val fontColor: String? = null,
)
