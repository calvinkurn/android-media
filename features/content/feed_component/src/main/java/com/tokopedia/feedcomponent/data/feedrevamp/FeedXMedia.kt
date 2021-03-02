package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXMedia(
        @SerializedName("id")
        var id: String,
        @SerializedName("type")
        var type: String,
        @SerializedName("appLink")
        var appLink: String,
        @SerializedName("webLink")
        var webLink: String,
        @SerializedName("coverURL")
        var coverUrl: String,
        @SerializedName("mediaURL")
        var mediaUrl: String,
        @SerializedName("tagging")
        var tagging: List<FeedXMediaTagging>,
        @SerializedName("mods")
        var mods: List<String>,
)

