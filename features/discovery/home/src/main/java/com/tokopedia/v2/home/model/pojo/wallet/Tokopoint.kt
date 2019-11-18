package com.tokopedia.v2.home.model.pojo.wallet

import com.google.gson.annotations.SerializedName

data class Tokopoint (
    @SerializedName("redirectURL")
    private var redirectURL: String = "",
    @SerializedName("iconImageURL")
    private val iconImageURL: String = "",
    @SerializedName("sectionContent")
    private val sectionContent: List<SectionContentItem> = listOf(),
    @SerializedName("offFlag")
    private val offFlag: Boolean = false,
    @SerializedName("redirectAppLink")
    private val redirectAppLink: String = ""
)

data class SectionContentItem(
    @SerializedName("textAttributes")
    private var textAttributes: TextAttributes = TextAttributes(),
    @SerializedName("tagAttributes")
    private val tagAttributes: TagAttributes = TagAttributes(),
    @SerializedName("type")
    private val type: String = ""
)

data class TextAttributes(
    @SerializedName("text")
    private var text: String = "",
    @SerializedName("backgroundColor")
    private val backgroundColour: String = ""
)

data class TagAttributes(
    @SerializedName("text")
    private var text: String = "",
    @SerializedName("backgroundColor")
    private val backgroundColour: String = ""
)