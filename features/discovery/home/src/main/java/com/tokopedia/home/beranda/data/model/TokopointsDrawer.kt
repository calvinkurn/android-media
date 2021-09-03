package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokopointsDrawer(
        @Expose
        @SerializedName("type")
        val type: String = "",
        @Expose
        @SerializedName("redirectURL")
        val redirectURL: String = "",
        @Expose
        @SerializedName("iconImageURL")
        val iconImageURL: String = "",
        @Expose
        @SerializedName("sectionContent")
        val sectionContent: List<SectionContentItem> = listOf(),
        @Expose
        @SerializedName("redirectAppLink")
        val redirectAppLink: String = "",
        val mainPageTitle: String = "",
        val isError: Boolean = false
)