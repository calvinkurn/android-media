package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class FeedXHeader(

    @SerializedName("data")
    var data: FeedXHeaderData? = FeedXHeaderData(),
    @SerializedName("error")
    var error: String? = null,
    @SerializedName("__typename")
    var _typename: String? = null
)
data class FeedXHeaderData(
    @SerializedName("creation")
    var creation: Creation? = Creation(),
    @SerializedName("tab")
    var tab: Tab? = Tab(),
    @SerializedName("live")
    var live: Live? = Live(),
    @SerializedName("userProfile")
    var userProfile: UserProfile? = UserProfile(),
    @SerializedName("__typename")
    var _typename: String? = null
)
