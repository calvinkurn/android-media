package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

/**
 * Created by mzennis on 2019-12-03.
 */
data class Channel(

        @SerializedName("channel_id")
        var channelId: String = "",

        @SerializedName("title")
        var title: String = "",

        @SerializedName("description")
        var description: String = "",

        @SerializedName("channel_url")
        var channelUrl: String = "",

        @SerializedName("cover_url")
        var coverUrl: String = "",

        @SerializedName("start_time")
        var startTime: String = "",

        @SerializedName("end_time")
        var endTime: String = "",

        @SerializedName("total_participants_online")
        var totalParticipantsOnline: String = "",

        @SerializedName("total_views")
        var totalViews: String = "",

        @SerializedName("is_active")
        var isActive: String = "",

        @SerializedName("is_freeze")
        var isFreeze: String = "",

        @SerializedName("moderator_id")
        var moderatorId: String = "",

        @SerializedName("moderator_name")
        var moderatorName: String = "",

        @SerializedName("moderator_thumb_url")
        var moderatorThumbUrl: String = "",

        @SerializedName("gc_token")
        var gcToken: String = "",

        @SerializedName("banner_url")
        var bannerUrl: String = "",

        @SerializedName("ads_url")
        var adsUrl: String = "",

        @SerializedName("ads_link")
        var adsLink: String = "",

        @SerializedName("banner_name")
        var bannerName: String = "",

        @SerializedName("ads_id")
        var adsId: String = "",

        @SerializedName("ads_name")
        var adsName: String = "",

        @SerializedName("flashsale")
        var flashsale: Flashsale = Flashsale(),

        @SerializedName("banned_msg")
        var bannedMsg: String = "",

        @SerializedName("banned_title")
        var bannedTitle: String = "",

        @SerializedName("banned_button_title")
        var bannedButtonTitle: String = "",

        @SerializedName("banned_button_url")
        var bannedButtonUrl: String = "",

        @SerializedName("kick_msg")
        var kickMsg: String = "",

        @SerializedName("kick_title")
        var kickTitle: String = "",

        @SerializedName("kick_button_title")
        var kickButtonTitle: String = "",

        @SerializedName("kick_button_url")
        var kickButtonUrl: String = "",

        @SerializedName("kick_duration")
        var kickDuration: String = "",

        @SerializedName("pinned_message")
        var pinnedMessage: PinnedMessage = PinnedMessage(),

        @SerializedName("quick_reply")
        var quickReply: List<String> = emptyList(),

        @SerializedName("partner_id")
        var partnerId: String = "",

        @SerializedName("settings")
        var settings: Settings = Settings(),

        @SerializedName("exit_msg")
        var exitMsg: ExitMsg = ExitMsg(),

        @SerializedName("freeze_channel_state")
        var freezeChannelState: FreezeChannelState = FreezeChannelState(),

        @SerializedName("overlay_message")
        var overlayMessage: OverlayMessage = OverlayMessage(),

        @SerializedName("video_id")
        var videoId: String = "",

        @SerializedName("video_live")
        var videoLive: String = "",

        @SerializedName("background_default")
        var backgroundDefault: String = "",

        @SerializedName("background_url")
        var backgroundUrl: String = "",

        @SerializedName("link_info_url")
        var linkInfoUrl: String = "",

        @SerializedName("background")
        var background: Background = Background()

) {

    data class ChannelResponse(
            @SerializedName("channel")
            var channel: Channel = Channel()
    )

    data class Flashsale(
            @SerializedName("campaign_id")
            var campaignId: String = "",
            @SerializedName("campaign_name")
            var campaignName: String = "",
            @SerializedName("campaign_short_name")
            var campaignShortName: String = "",
            @SerializedName("start_date")
            var startDate: String = "",
            @SerializedName("end_date")
            var endDate: String = "",
            @SerializedName("status")
            var status: String = "",
            @SerializedName("products")
            var products: List<String> = emptyList(),
            @SerializedName("applink")
            var applink: String = "")

    data class PinnedMessage(
            @SerializedName("pinned_message_id")
            var pinnedMessageId: String = "",
            @SerializedName("title")
            var title: String = "",
            @SerializedName("message")
            var message: String = "",
            @SerializedName("image_url")
            var imageUrl: String = "",
            @SerializedName("redirect_url")
            var redirectUrl: String = "")

    data class Settings(
            @SerializedName("ping_interval")
            var pingInterval: Int = 0,
            @SerializedName("max_chars")
            var maxChars: Int = 0,
            @SerializedName("max_retries")
            var maxRetries: Int = 0,
            @SerializedName("min_reconnect_delay")
            var minReconnectDelay: Int = 0)

    data class ExitMsg(
            @SerializedName("title")
            var title: String = "",
            @SerializedName("body")
            var body: String = "")

    data class FreezeChannelState(
            @SerializedName("category")
            var category: String = "",
            @SerializedName("title")
            var title: String = "",
            @SerializedName("desc")
            var desc: String = "",
            @SerializedName("btn_title")
            var btnTitle: String = "",
            @SerializedName("btn_app_link")
            var btnAppLink: String = "")

    data class OverlayMessage(
            @SerializedName("overlay_id")
            var overlayId: String = "",
            @SerializedName("type_message")
            var type_message: String = "",
            @SerializedName("status")
            var status: String = "",
            @SerializedName("assets")
            var assets: Assets = Assets(),
            @SerializedName("closeable")
            var closeable: String = "",
            @SerializedName("type")
            var type: String = "",
            @SerializedName("link_url")
            var linkUrl: String = ""
    ) {

        data class Assets(
                @SerializedName("title")
                var title: String = "",
                @SerializedName("description")
                var description: String = "",
                @SerializedName("image_url")
                var imageUrl: String = "",
                @SerializedName("image_link")
                var imageLink: String = "",
                @SerializedName("btn_title")
                var btnTitle: String = "",
                @SerializedName("btn_link")
                var btnLink: String = ""
        )
    }

    data class Background(
            @SerializedName("background_default")
            var backgroundDefault: String = "",
            @SerializedName("background_url")
            var backgroundUrl: String = "")
}
