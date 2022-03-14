package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedTabs(
        @SerializedName("data")
        @Expose
        val feedData: List<FeedData> = listOf(),

        @SerializedName("meta")
        @Expose
        val meta: Meta = Meta()
) {

    data class Response(
            @SerializedName("feedTabs")
            @Expose
            val feedTabs: FeedTabs = FeedTabs()
    )

    data class FeedData(
            @SerializedName("isActive")
            @Expose
            val isActive: Boolean = false,

            @SerializedName("key")
            @Expose
            val key: String = "",

            @SerializedName("position")
            @Expose
            val position: Int = -1,

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("type")
            @Expose
            val type: String = ""
    )

    data class Meta(
            @SerializedName("selectedIndex")
            @Expose
            val selectedIndex: Int = 0
    )

    companion object {
        const val TYPE_FEEDS = "feeds"
        const val TYPE_EXPLORE = "explore"
        const val TYPE_VIDEO = "video"
        const val TYPE_CUSTOM = "custom"
        const val KEY_TRENDING = "trending";
    }
}