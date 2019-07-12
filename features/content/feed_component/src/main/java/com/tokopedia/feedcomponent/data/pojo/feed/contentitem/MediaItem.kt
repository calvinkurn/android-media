package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.track.Tracking

/**
 * @author by yfsx on 04/12/18.
 */
data class MediaItem(
        @SerializedName("id")
        @Expose
        var id: String = "",

        @SerializedName("text")
        @Expose
        var text: String = "",

        @SerializedName("price")
        @Expose
        var price: String = "",

        @SerializedName("type")
        @Expose
        var type: String = "",

        @SerializedName("appLink")
        @Expose
        var applink: String = "",

        @SerializedName("webLink")
        @Expose
        var weblink: String = "",

        @SerializedName("thumbnail")
        @Expose
        var thumbnail: String = "",

        @SerializedName("percentage")
        val percentage: String = "",

        @SerializedName("isSelected")
        var isSelected: Boolean = false,

        @SerializedName("position")
        @Expose
        var position: MutableList<Float> = ArrayList(),

        @SerializedName("tracking")
        @Expose
        val tracking: List<Tracking> = ArrayList(),

        @SerializedName("videod")
        @Expose
        val videos: List<Video> = ArrayList(),

        @SerializedName("isCanPlayVideo")
        @Expose
        var isCanPlayVideo: Boolean = false
)