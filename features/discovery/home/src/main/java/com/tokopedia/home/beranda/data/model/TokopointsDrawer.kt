package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
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
    @Expose(serialize = false, deserialize = false)
    val mainPageTitle: String = ""
){

    override fun toString(): String {
        return "TokopointsDrawer{" +
                "redirectURL = '" + redirectURL + '\'' +
                ",iconImageURL = '" + iconImageURL + '\'' +
                ",sectionContent = '" + sectionContent + '\'' +
                ",redirectAppLink = '" + redirectAppLink + '\'' +
                "}"
    }
}