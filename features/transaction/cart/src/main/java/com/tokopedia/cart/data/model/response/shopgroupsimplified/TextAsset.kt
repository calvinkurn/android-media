package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class TextAsset(
    @SerializedName("square_icon")
    val squareIcon: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("font_color")
    val fontColor: String = "",
    @SerializedName("background_start_color")
    val backgroundStartColor: String = "",
    @SerializedName("background_end_color")
    val backgroundEndColor: String = "",
    @SerializedName("line_color")
    val lineColor: String = ""
)
