package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.SerializedName

data class PlayLiveDynamicChannelEntity(
        @SerializedName("playGetLiveDynamicChannels")
        val playDynamicData: PlayDynamicData = PlayDynamicData()
)

data class PlayDynamicData(
        @SerializedName("data")
        val playData: PlayData = PlayData()
)

data class PlayData(
    @SerializedName("channels")
    val playChannels: List<PlayChannel> = listOf()
)

data class PlayChannel(
        @SerializedName("channel_id")
        val channelId: String = "-1",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("cover_url")
        val coverUrl: String = "",
        @SerializedName("total_view_formatted")
        val totalView: String = "",
        @SerializedName("moderator_id")
        val moderatorId: String = "",
        @SerializedName("moderator_name")
        val moderatorName: String = "",
        @SerializedName("video_stream")
        val videoStream: VideoStream = VideoStream()
)

data class VideoStream(
        @SerializedName("is_live")
        val isLive: Boolean = false,
        @SerializedName("config")
        val config: Config = Config()
)

data class Config(
        @SerializedName("stream_url")
        val streamUrl: String = ""
)