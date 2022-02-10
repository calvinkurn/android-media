package com.tokopedia.play.widget.sample.data

import com.google.gson.annotations.SerializedName


data class PlayVideoStream(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("orientation")
        var orientation: String = "",
        @SerializedName("type")
        var type: String = "",
        @SerializedName("cover_url")
        var cover_url: String = "",
        @SerializedName("stream_source")
        var stream_source: String = "",
        @SerializedName("autoplay")
        var autoplay: Boolean = true,
        @SerializedName("buffer_control")
        var buffer_control: PlayVideoBufferControl = PlayVideoBufferControl(),


)
data class PlayVideoBufferControl(
        @SerializedName("max_buffer_in_seconds")
        var max_buffer_in_seconds: Int = 0,
        @SerializedName("min_buffer_in_seconds")
        var min_buffer_in_seconds: Int = 0,
        @SerializedName("buffer_for_playback")
        var buffer_for_playback: Int = 0,
        @SerializedName("buffer_for_playback_after_rebuffer")
        var buffer_for_playback_after_rebuffer: Int = 0,

)

