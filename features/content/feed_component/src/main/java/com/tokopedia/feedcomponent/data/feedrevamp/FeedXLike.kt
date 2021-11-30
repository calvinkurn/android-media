package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXLike(
        @SerializedName("label")
        var label: String = "",
        @SerializedName("count")
        var count: Int = 0,
        @SerializedName("countFmt")
        var countFmt: String = "",
        @SerializedName("likedBy")
        var likedBy: List<String> = emptyList(),
        @SerializedName("isLiked")
        var isLiked: Boolean = false,
        @SerializedName("mods")
        var mods: List<String> = emptyList(),
)
