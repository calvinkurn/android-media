package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXLike(
        @SerializedName("label")
        var label: String,
        @SerializedName("count")
        var count: Int,
        @SerializedName("countFmt")
        var countFmt: String,
        @SerializedName("likedBy")
        var likedBy: String,
        @SerializedName("isLiked")
        var isLiked: Boolean,
        @SerializedName("mods")
        var mods: List<String>,
)
