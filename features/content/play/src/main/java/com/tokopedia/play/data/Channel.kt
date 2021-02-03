package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 03/08/20.
 */
data class Channel(
        @SerializedName("id")
        val channelId: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("start_time")
        val startTime: String = "",
        @SerializedName("end_time")
        val endTime: String = "",
        @SerializedName("is_live")
        val isLive: Boolean = false,
        @SerializedName("partner")
        val partner: Partner = Partner(),
        @SerializedName("video")
        val video: Video = Video(),
        @SerializedName("pinned_message")
        val pinnedMessage: PinnedMessage = PinnedMessage(),
        @SerializedName("quick_replies")
        val quickReplies: List<String> = emptyList(),
        @SerializedName("configurations")
        val configuration: Configuration = Configuration(),
        @SerializedName("stats")
        val stats: Statistic = Statistic(),
        @SerializedName("app_link")
        val appLink: String = "",
        @SerializedName("webLink")
        val webLink: String = "",
        @SerializedName("share")
        val share: Share = Share()
) {

    data class Data(
            @SerializedName("data")
            val channel: Channel = Channel()
    )

    data class Response(
            @SerializedName("playGetChannelDetails")
            val data: Data = Data()
    )

    data class Partner(
            @SerializedName("id")
            val id: String = "",
            @SerializedName("type")
            val type: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("thumbnail_url")
            val thumbnailUrl: String = "",
            @SerializedName("badge_url")
            val badgeUrl: String = ""
    )

    data class PinnedMessage(
            @SerializedName("id")
            val id: String = "",
            @SerializedName("title")
            val title: String = "",
            @SerializedName("message")
            val message: String = "",
            @SerializedName("redirect_url")
            val redirectUrl: String = ""
    )

    data class Configuration(
            @SerializedName("ping_interval")
            val pingInterval: Long = 0,
            @SerializedName("max_chars")
            val maxChars: Int = 0,
            @SerializedName("max_retries")
            val maxRetries: Int = 0,
            @SerializedName("min_reconnect_delay")
            val minReconnectDelay: Int = 0,
            @SerializedName("show_cart")
            val showCart: Boolean = false,
            @SerializedName("show_pinned_product")
            val showPinnedProduct: Boolean = false,
            @SerializedName("published")
            val published: Boolean = false,
            @SerializedName("active")
            val active: Boolean = false,
            @SerializedName("freezed")
            val freezed: Boolean = false,
            @SerializedName("has_promo")
            val hasPromo: Boolean = false,
            @SerializedName("feeds_like_params")
            val feedsLikeParams: FeedLikeParam = FeedLikeParam(),
            @SerializedName("channel_freeze_screen")
            val channelFreezeScreen: ChannelFreezeScreen = ChannelFreezeScreen(),
            @SerializedName("channel_banned_message")
            val channelBannedMessage: ChannelBannedMessage = ChannelBannedMessage(),
            @SerializedName("pinned_product_config")
            val pinnedProduct: PinnedProduct = PinnedProduct(),
            @SerializedName("room_background")
            val roomBackground: RoomBackground = RoomBackground())

    data class FeedLikeParam(
            @SerializedName("content_type")
            val contentType: Int = 0,
            @SerializedName("content_id")
            val contentId: String = "",
            @SerializedName("like_type")
            val likeType: Int = 1
    )

    data class Statistic(
            @SerializedName("view")
            val view: TotalView = TotalView()
    )

    data class TotalView(
            @SerializedName("value")
            val value: String = "",
            @SerializedName("formatted")
            val formatted: String = ""
    )

    data class ChannelFreezeScreen(
            @SerializedName("category")
            val category: String = "",
            @SerializedName("title")
            val title: String = "",
            @SerializedName("description")
            val desc: String = "",
            @SerializedName("button_text")
            val btnTitle: String = "",
            @SerializedName("button_app_link")
            val btnAppLink: String = "")

    data class ChannelBannedMessage(
            @SerializedName("title")
            val title: String = "",
            @SerializedName("message")
            val message: String = "",
            @SerializedName("button_text")
            val buttonText: String = ""
    )

    data class PinnedProduct(
            @SerializedName("pin_title")
            val title: String = "",
            @SerializedName("bottom_sheet_title")
            val titleBottomSheet: String = ""
    )

    data class RoomBackground(
            @SerializedName("image_url")
            val imageUrl: String = ""
    )

    data class Share(
            @SerializedName("text")
            val text: String = "",
            @SerializedName("redirect_url")
            val redirectUrl: String = "",
            @SerializedName("use_short_url")
            val useShortUrl: Boolean = false,
            @SerializedName("meta_title")
            val metaTitle: String = "",
            @SerializedName("meta_description")
            val metaDescription: String = "",
            @SerializedName("is_show_button")
            val isShowButton: Boolean = false
    )
}