package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXCardDataItem(
        @SerializedName("id")
        var id: String,
        @SerializedName("appLink")
        var appLink: String,
        @SerializedName("webLink")
        var webLink: String,
        @SerializedName("coverURL")
        var coverUrl: String,
        @SerializedName("mods")
        var mods: List<String>,
        @SerializedName("products")
        var products: List<FeedXProduct>,
)
