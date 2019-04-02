package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.track.Tracking

data class Media(
        @SerializedName("id")
        @Expose
        var id: String = "",
        @SerializedName("type")
        @Expose
        var type: String = "",
        @SerializedName("appLink")
        @Expose
        var appLink: String = "",
        @SerializedName("webLink")
        @Expose
        var webLink: String = "",
        @SerializedName("thumbnail")
        @Expose
        var thumbnail: String = "",
        @SerializedName("totalItems")
        @Expose
        var totalItems: Int = 0,
        @SerializedName("isSelected")
        @Expose
        var isIsSelected: Boolean = false,
        @SerializedName("isVoted")
        val isVoted: Boolean = false,
        @SerializedName("totalVoter")
        val totalVoter: Int = 0,
        @SerializedName("text")
        val text: String = "",
        @SerializedName("items")
        @Expose
        var mediaItems: List<MediaItem> = ArrayList(),
        @SerializedName("ctaLink")
        @Expose
        var ctaLink: CtaLink = CtaLink(),
        @SerializedName("tags")
        val tags: List<Tag> = ArrayList(),
        @SerializedName("tracking")
        @Expose
        val tracking: List<Tracking> = ArrayList()
)