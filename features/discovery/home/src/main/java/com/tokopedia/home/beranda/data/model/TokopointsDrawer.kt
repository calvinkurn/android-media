package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.SerializedName

data class TokopointsDrawer(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("redirectURL")
        val redirectURL: String = "",
        @SerializedName("iconImageURL")
        val iconImageURL: String = "",
        @SerializedName("sectionContent")
        val sectionContent: List<SectionContentItem> = listOf(),
        @SerializedName("redirectAppLink")
        val redirectAppLink: String = "",
        val mainPageTitle: String = "",
        val isError: Boolean = false
)