package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXComments(
        @SerializedName("label")
        var label: String,
        @SerializedName("count")
        var count: Int,
        @SerializedName("countFmt")
        var countFmt: String,
        @SerializedName("items")
        var commentItems: FeedXCommentsItem,
        @SerializedName("mods")
        var mods: List<String>,
)
