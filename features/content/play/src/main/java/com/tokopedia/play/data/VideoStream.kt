package com.tokopedia.play.data

import com.google.android.exoplayer2.DefaultLoadControl
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
        val bufferControl: BufferControl? = BufferControl()
) {

    data class Config(
            @SerializedName("youtube_id")
            val youtubeId: String = "", // type: youtube
            @SerializedName("stream_url")
            val streamUrl: String = "", // type: vod, live
            @SerializedName("livestream_id")
            val liveStreamId: String = "", // type: live
            @SerializedName("is_auto_play")
            val isAutoPlay: Boolean = false
    )

    data class BufferControl(
            @SerializedName("max_buffer_in_second")
            val maxBufferingSecond: Int = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
            @SerializedName("min_buffer_in_second")
            val minBufferingSecond: Int = DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
            @SerializedName("buffer_for_playback")
            val bufferForPlayback: Int = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
            @SerializedName("buffer_for_playback_after_rebuffer")
            val bufferForPlaybackAfterRebuffer: Int = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS
    )
}