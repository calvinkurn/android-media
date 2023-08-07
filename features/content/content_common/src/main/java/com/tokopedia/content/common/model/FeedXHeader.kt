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
    @SerializedName("__typename")
    val typename: String = ""
)
