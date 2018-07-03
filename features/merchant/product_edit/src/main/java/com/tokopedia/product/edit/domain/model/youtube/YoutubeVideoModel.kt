package com.tokopedia.product.edit.domain.model.youtube


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class YoutubeVideoModel {

    @SerializedName("kind")
    @Expose
    private val kind: String? = null
    @SerializedName("etag")
    @Expose
    private val etag: String? = null
    @SerializedName("pageInfo")
    @Expose
    private val pageInfo: PageInfo? = null
    @SerializedName("items")
    @Expose
    var items: List<Item>? = null

    private val item: Item? = null

    val isVideoAgeRestricted: Boolean
        get() {
            if (!hasVideo()) {
                return false
            }
            val contentRating = items!![0].contentDetails!!.contentRating
            return contentRating != null && contentRating.ytRating == YT_AGE_RESTRICTED
        }

    val id: String?
        get() = items!![0].id

    val title: String?
        get() = items!![0].snippet!!.title

    val description: String?
        get() = items!![0].snippet!!.description

    val channel: String?
        get() = items!![0].snippet!!.channelTitle

    val height: Int
        get() = items!![0].snippet!!.thumbnails!!.default!!.height

    val width: Int
        get() = items!![0].snippet!!.thumbnails!!.default!!.width

    val thumbnailUrl: String?
        get() = items!![0].snippet!!.thumbnails!!.default!!.url

    val duration: String?
        get() = items!![0].contentDetails!!.duration

    private fun hasVideo(): Boolean {
        return items != null && items!!.size > 0
    }

    companion object {

        private const val YT_AGE_RESTRICTED = "ytAgeRestricted"
    }
}