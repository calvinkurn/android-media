package com.tokopedia.v2.home.model.pojo.wallet

import com.google.gson.annotations.SerializedName

data class TokopointData(
    @SerializedName("tokopointsDrawer")
    val tokopoint: Tokopoint
)

data class Tokopoint (
    @SerializedName("redirectURL")
    val redirectURL: String = "",
    @SerializedName("iconImageURL")
    val iconImageURL: String = "",
    @SerializedName("sectionContent")
    val sectionContent: List<SectionContentItem> = listOf(),
    @SerializedName("offFlag")
    val offFlag: Boolean = false,
    @SerializedName("redirectAppLink")
    val redirectAppLink: String = ""
)

data class SectionContentItem(
    @SerializedName("textAttributes")
    val textAttributes: TextAttributes = TextAttributes(),
    @SerializedName("tagAttributes")
    val tagAttributes: TagAttributes = TagAttributes(),
    @SerializedName("type")
    val type: String = ""
)

data class TextAttributes(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("backgroundColor")
    val backgroundColour: String = "",
    @SerializedName("isBold")
    val isBold: Boolean = false,
    @SerializedName("color")
    val color: String = "#adadad"
)

data class TagAttributes(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("backgroundColor")
    val backgroundColour: String = ""
)