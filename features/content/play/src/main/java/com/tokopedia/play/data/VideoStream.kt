package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-05.
 */
data class VideoStream(
        @SerializedName("orientation")
        val orientation: String = "", // vertical or horizontal
        @SerializedName("type")
        val type: String = "", // youtube / live / live+playback / vod / playback+live
        @SerializedName("is_live")
        val isLive: Boolean = false,
        @SerializedName("config")
        val config: Config = Config(streamUrl = "rtmp://fms.105.net/live/rmc1") // TODO("remove this, testing purposes")
) {

    data class Response(
            @SerializedName("video_stream")
            val videoStream: VideoStream = VideoStream()
    )

    data class Config(
            @SerializedName("youtube_id")
            val youtubeId: String = "", // type: youtube
            @SerializedName("stream_url")
            val streamUrl: String = "", // type: vod, live
            @SerializedName("livestream_id")
            val liveStreamId: String = "" // type: live
    )
}