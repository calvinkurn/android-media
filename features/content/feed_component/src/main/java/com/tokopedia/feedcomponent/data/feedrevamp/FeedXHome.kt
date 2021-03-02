package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName

data class FeedXHome(
        @SerializedName("items")
        var items: List<FeedXCard>,
        @SerializedName("mods")
        var mods: List<String>,
        @SerializedName("pagination")
        var pagination: FeedXPaginationInfo
)