package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.SerializedName

data class TokopointsDrawer(
    @SerializedName("redirectURL")
    val redirectURL: String = "",
    @SerializedName("iconImageURL")
    val iconImageURL: String = "",
    @SerializedName("sectionContent")
    val sectionContent: List<SectionContentItem> = listOf(),
    @SerializedName("redirectAppLink")
    val redirectAppLink: String = "",
    val mainPageTitle: String = ""
)