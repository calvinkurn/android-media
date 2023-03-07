package com.tokopedia.home.beranda.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SubscriptionsDrawerList(
    @SerializedName("IconImageURL")
    @Expose
    val iconImageURL: String = "",
    @SerializedName("RedirectAppLink")
    @Expose
    val redirectAppLink: String = "",
    @SerializedName("RedirectURL")
    @Expose
    val redirectURL: String = "",
    @SerializedName("SectionContent")
    @Expose
    val sectionContent: List<SectionContent> = listOf()
)