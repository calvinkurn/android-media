package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXShare(
        @SerializedName("label")
        var label: String = "",
        @SerializedName("operation")
        var operation: String = "",
        @SerializedName("mods")
        var mods: List<String> = emptyList(),
)
