package com.tokopedia.youtube_common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class YoutubeVideoDetailModel(
        @SerializedName("kind")
        @Expose
        private val kind: String? = null,
        @SerializedName("etag")
        @Expose
        private val etag: String? = null,
        @SerializedName("pageInfo")
        @Expose
        private val pageInfo: PageInfo? = null,
        @SerializedName("items")
        @Expose
        var items: List<Item>? = null) {

    val id: String?
        get() = items?.firstOrNull()?.id

    val title: String?
        get() = items?.firstOrNull()?.snippet?.title

    val description: String?
        get() = items?.firstOrNull()?.snippet?.description

    val channel: String?
        get() = items?.firstOrNull()?.snippet?.channelTitle

    val height: Int
        get() = items?.firstOrNull()?.snippet?.thumbnails?.default?.height ?: 0

    val width: Int
        get() = items?.firstOrNull()?.snippet?.thumbnails?.default?.width ?: 0

    val thumbnailUrl: String?
        get() = items?.firstOrNull()?.snippet?.thumbnails?.default?.url

    val duration: String?
        get() = items?.firstOrNull()?.contentDetails?.duration.toString()
}