package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-05.
 */
data class VideoStream(
        @SerializedName("orientation")
        val orientation: String = "",
        @SerializedName("android_stream_sd")
        val androidStreamSd: String = "",
        @SerializedName("android_stream_hd")
        val androidStreamHd: String = "",
        @SerializedName("is_live")
        val isLive: Boolean = false,
        @SerializedName("is_active")
        val isActive: Boolean = false
) {
    data class Response(
            @SerializedName("video_stream")
            val videoStream: VideoStream = VideoStream()
    )
}