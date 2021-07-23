package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXCommentsItem(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("text")
        var text: String = "",
        @SerializedName("author")
        var author: FeedXAuthor = FeedXAuthor(),
        @SerializedName("mods")
        var mods: List<String> = emptyList(),
)
