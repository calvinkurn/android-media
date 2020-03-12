package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-05.
 */
data class VideoStream(
        @SerializedName("video_stream_id")
        val id: String = "", // vertical or horizontal
        @SerializedName("orientation")
        val orientation: String = "", // vertical or horizontal
        @SerializedName("type")
        val type: String = "live", // youtube / live / live+playback / vod / playback+live
        @SerializedName("is_live")
        val isLive: Boolean = false,
        @SerializedName("config")
        val config: Config = Config(),
        @SerializedName("buffer_control")
        val bufferControl: BufferControl = BufferControl()
) {

    data class Config(
            @SerializedName("youtube_id")
            val youtubeId: String = "", // type: youtube
            @SerializedName("stream_url")
            val streamUrl: String = "", // type: vod, live
            @SerializedName("livestream_id")
            val liveStreamId: String = "" // type: live
    )

    data class BufferControl(
            @SerializedName("max_buffer_in_second")
            val maxBufferingSecond: Int = 0,
            @SerializedName("min_buffer_in_second")
            val minBufferingSecond: Int = 0,
            @SerializedName("buffer_for_playback")
            val bufferForPlayback: Int = 0,
            @SerializedName("buffer_for_playback_after_rebuffer")
            val bufferForPlaybackAfterRebuffer: Int = 0
    )
}