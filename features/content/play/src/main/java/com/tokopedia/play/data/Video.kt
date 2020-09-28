package com.tokopedia.play.data

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-05.
 */
data class Video(
        @SerializedName("id")
        val id: String = "", // vertical or horizontal
        @SerializedName("orientation")
        val orientation: String = "", // vertical or horizontal
        @SerializedName("type")
        val type: String = "", // youtube / live / live+playback / vod / playback+live
        @SerializedName("cover_url")
        val coverUrl: String = "",
        @SerializedName("stream_source")
        val streamSource: String = "",
        @SerializedName("autoplay")
        val autoPlay: Boolean = false,
        @SerializedName("buffer_control")
        val bufferControl: BufferControl? = BufferControl()
) {

    data class BufferControl(
            @SerializedName("max_buffer_in_seconds")
            val maxBufferingSecond: Int = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
            @SerializedName("min_buffer_in_seconds")
            val minBufferingSecond: Int = DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
            @SerializedName("buffer_for_playback")
            val bufferForPlayback: Int = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
            @SerializedName("buffer_for_playback_after_rebuffer")
            val bufferForPlaybackAfterRebuffer: Int = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS
    )
}