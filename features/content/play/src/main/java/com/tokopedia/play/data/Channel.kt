package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * Created by mzennis on 2019-12-03.
 */
data class Channel(

        @SerializedName("gc_token")
        val gcToken: String = "",

        @SerializedName("partner_type")
        val partnerType: Int = 0,

        @SerializedName("partner_id")
        val partnerId: Long = 0L,

        @SerializedName("channel_id")
        val channelId: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("description")
        val description: String = "",

        @SerializedName("cover_url")
        val coverUrl: String = "",

        @SerializedName("start_time")
        val startTime: Long = 0L,

        @SerializedName("end_time")
        val endTime: Long = 0L,

        @SerializedName("total_view_formatted")
        val totalViews: String = "",

        @SerializedName("total_participants_online")
        val totalParticipantsOnline: String = "",

        @SerializedName("is_active")
        val isActive: Boolean = true,

        @SerializedName("is_freeze")
        val isFreeze: Boolean = false,

        @SerializedName("moderator_id")
        val moderatorId: String = "",

        @SerializedName("moderator_name")
        val moderatorName: String = "",

        @SerializedName("moderator_thumb_url")
        val moderatorThumbUrl: String = "",

        @SerializedName("video_stream")
        var videoStream: VideoStream = VideoStream(), // TODO("testing purposes")

        @SerializedName("pinned_message")
        val pinnedMessage: PinnedMessage = PinnedMessage(),

        @SerializedName("quick_reply")
        val quickReply: List<String> = emptyList(),

        @SerializedName("settings")
        val settings: Settings = Settings(),

        @SerializedName("banned")
        val banned: Banned = Banned(),

        @SerializedName("exit_msg")
        val exitMsg: ExitMsg = ExitMsg(),

        @SerializedName("freeze_channel_state")
        val freezeChannelState: FreezeChannelState = FreezeChannelState()

) {

    data class Response(
            @SerializedName("channel")
            val channel: Channel = Channel()
    )

    data class Settings(
            @SerializedName("ping_interval")
            val pingInterval: Long = 0,
            @SerializedName("max_chars")
            val maxChars: Int = 0,
            @SerializedName("max_retries")
            val maxRetries: Int = 0,
            @SerializedName("min_reconnect_delay")
            val minReconnectDelay: Int = 0)

    data class ExitMsg(
            @SerializedName("title")
            val title: String = "",
            @SerializedName("body")
            val body: String = "")

    data class FreezeChannelState(
            @SerializedName("category")
            val category: String = "",
            @SerializedName("title")
            val title: String = "",
            @SerializedName("desc")
            val desc: String = "",
            @SerializedName("btn_title")
            val btnTitle: String = "",
            @SerializedName("btn_app_link")
            val btnAppLink: String = "")

        data class Banned(
                @SerializedName("msg")
                val message: String = "",

                @SerializedName("title")
                val title: String = "",

                @SerializedName("button_title")
                val buttonTitle: String = ""
        )
}
