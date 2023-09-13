package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

data class FeedXHeader(
    @SerializedName("data")
    val data: FeedXHeaderData = FeedXHeaderData(),
    @SerializedName("error")
    val error: String = "",
    @SerializedName("__typename")
    val typename: String = ""
)

data class FeedXHeaderData(
    @SerializedName("creation")
    val creation: Creation = Creation(),
    @SerializedName("tab")
    val tab: Tab = Tab(),
    @SerializedName("live")
    val live: Live = Live(),
    @SerializedName("userProfile")
    val userProfile: UserProfile = UserProfile(),
    @SerializedName("browse")
    val browse: FeedXHeaderBrowse = FeedXHeaderBrowse(),
    @SerializedName("cdpTitle")
    val detail: FeedXHeaderDetail = FeedXHeaderDetail(),
    @SerializedName("__typename")
    val typename: String = ""
)

data class FeedXHeaderBrowse(
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("__typename")
    val typeName: String = ""
)

data class FeedXHeaderDetail(
    @SerializedName("title")
    val title: String = ""
)
