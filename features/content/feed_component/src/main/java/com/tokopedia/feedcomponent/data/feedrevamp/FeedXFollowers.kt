package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXFollowers(
        @SerializedName("isFollowed")
        var isFollowed: Boolean,
        @SerializedName("label")
        var label: String,
        @SerializedName("count")
        var count: Int,
        @SerializedName("countFmt")
        var countFmt: String,
        @SerializedName("mods")
        var mods: List<String>,
)
