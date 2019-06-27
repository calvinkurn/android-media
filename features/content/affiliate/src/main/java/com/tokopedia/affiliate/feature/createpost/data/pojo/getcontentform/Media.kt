package com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform

import com.google.gson.annotations.SerializedName

data class Media(
        @SerializedName("allow_image")
        val allowImage: Boolean = false,
        @SerializedName("allow_video")
        val allowVideo: Boolean = false,
        @SerializedName("max_media")
        val maxMedia: Int = 0,
        @SerializedName("media")
        val media: List<MediaItem> = listOf(),
        @SerializedName("multiple_media")
        val multipleMedia: Boolean = false
)