package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class FeedXHeaderResponse(
    @SerializedName("feedXHeader")
    val feedXHeaderData: FeedXHeader = FeedXHeader()
)
enum class FeedXHeaderRequestFields(val value: String) {
    CREATION("creation"),
    TAB("tab"),
    LIVE("live"),
    USER("user-profile")
}
