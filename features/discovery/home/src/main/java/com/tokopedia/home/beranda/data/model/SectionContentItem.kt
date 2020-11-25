package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.SerializedName


data class SectionContentItem (
    @SerializedName("textAttributes")
    val textAttributes: TextAttributes? = null,
    @SerializedName("tagAttributes")
    val tagAttributes: TagAttributes? = null,
    @SerializedName("type")
    val type: String = ""
)

data class TagAttributes(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("backgroundColor")
    val backgroundColour: String = ""
)


data class TextAttributes(
    @SerializedName("color")
    val colour: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("isBold")
    val isBold: Boolean = false
)