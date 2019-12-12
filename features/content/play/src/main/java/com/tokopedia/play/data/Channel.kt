package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * Created by mzennis on 2019-12-03.
 */
data class Channel(

        @SerializedName("channel_id")
        val channelId: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("description")
        val description: String = "",

        @SerializedName("channel_url")
        val channelUrl: String = "",

        @SerializedName("cover_url")
        val coverUrl: String = "",

        @SerializedName("start_time")
        val startTime: String = "",

        @SerializedName("end_time")
        val endTime: String = "",

        @SerializedName("total_participants_online")
        val totalParticipantsOnline: String = "",

        @SerializedName("total_views")
        val totalViews: String = "",

        @SerializedName("is_active")
        val isActive: String = "",

        @SerializedName("is_freeze")
        val isFreeze: String = "",

        @SerializedName("moderator_id")
        val moderatorId: String = "",

        @SerializedName("moderator_name")
        val moderatorName: String = "",

        @SerializedName("moderator_thumb_url")
        val moderatorThumbUrl: String = "",

        @SerializedName("gc_token")
        val gcToken: String = "",

        @SerializedName("banner_url")
        val bannerUrl: String = "",

        @SerializedName("ads_url")
        val adsUrl: String = "",

        @SerializedName("ads_link")
        val adsLink: String = "",

        @SerializedName("banner_name")
        val bannerName: String = "",

        @SerializedName("ads_id")
        val adsId: String = "",

        @SerializedName("ads_name")
        val adsName: String = "",

        @SerializedName("flashsale")
        val flashsale: Flashsale = Flashsale(),

        @SerializedName("banned_msg")
        val bannedMsg: String = "",

        @SerializedName("banned_title")
        val bannedTitle: String = "",

        @SerializedName("banned_button_title")
        val bannedButtonTitle: String = "",

        @SerializedName("banned_button_url")
        val bannedButtonUrl: String = "",

        @SerializedName("kick_msg")
        val kickMsg: String = "",

        @SerializedName("kick_title")
        val kickTitle: String = "",

        @SerializedName("kick_button_title")
        val kickButtonTitle: String = "",

        @SerializedName("kick_button_url")
        val kickButtonUrl: String = "",

        @SerializedName("kick_duration")
        val kickDuration: String = "",

        @SerializedName("pinned_message")
        val pinnedMessage: PinnedMessage = PinnedMessage(),

        @SerializedName("quick_reply")
        val quickReply: List<String> = emptyList(),

        @SerializedName("partner_id")
        val partnerId: String = "",

        @SerializedName("partner_type")
        val partnerType: String = "",

        @SerializedName("settings")
        val settings: Settings = Settings(),

        @SerializedName("exit_msg")
        val exitMsg: ExitMsg = ExitMsg(),

        @SerializedName("freeze_channel_state")
        val freezeChannelState: FreezeChannelState = FreezeChannelState(),

        @SerializedName("overlay_message")
        val overlayMessage: OverlayMessage = OverlayMessage(),

        @SerializedName("video_id")
        val videoId: String = "",

        @SerializedName("video_live")
        val videoLive: String = "",

        @SerializedName("background_default")
        val backgroundDefault: String = "",

        @SerializedName("background_url")
        val backgroundUrl: String = "",

        @SerializedName("link_info_url")
        val linkInfoUrl: String = "",

        @SerializedName("background")
        val background: Background = Background()

) {

    data class Response(
            @SerializedName("channel")
            val channel: Channel = Channel()
    )

    data class Flashsale(
            @SerializedName("campaign_id")
            val campaignId: String = "",
            @SerializedName("campaign_name")
            val campaignName: String = "",
            @SerializedName("campaign_short_name")
            val campaignShortName: String = "",
            @SerializedName("start_date")
            val startDate: String = "",
            @SerializedName("end_date")
            val endDate: String = "",
            @SerializedName("status")
            val status: String = "",
            @SerializedName("products")
            val products: List<String> = emptyList(),
            @SerializedName("applink")
            val applink: String = "")

    data class Settings(
            @SerializedName("ping_interval")
            val pingInterval: Int = 0,
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

    data class OverlayMessage(
            @SerializedName("overlay_id")
            val overlayId: String = "",
            @SerializedName("type_message")
            val type_message: String = "",
            @SerializedName("status")
            val status: String = "",
            @SerializedName("assets")
            val assets: Assets = Assets(),
            @SerializedName("closeable")
            val closeable: String = "",
            @SerializedName("type")
            val type: String = "",
            @SerializedName("link_url")
            val linkUrl: String = ""
    ) {

        data class Assets(
                @SerializedName("title")
                val title: String = "",
                @SerializedName("description")
                val description: String = "",
                @SerializedName("image_url")
                val imageUrl: String = "",
                @SerializedName("image_link")
                val imageLink: String = "",
                @SerializedName("btn_title")
                val btnTitle: String = "",
                @SerializedName("btn_link")
                val btnLink: String = ""
        )
    }

    data class Background(
            @SerializedName("background_default")
            val backgroundDefault: String = "",
            @SerializedName("background_url")
            val backgroundUrl: String = "")
}
