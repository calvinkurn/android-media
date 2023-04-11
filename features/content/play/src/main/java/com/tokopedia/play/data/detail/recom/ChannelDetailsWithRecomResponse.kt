package com.tokopedia.play.data.detail.recom

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.gson.annotations.SerializedName
import com.tokopedia.play.data.multiplelikes.MultipleLikeConfig
import com.tokopedia.play.data.realtimenotif.RealTimeNotification

/**
 * Created by jegul on 20/01/21
 */
data class ChannelDetailsWithRecomResponse(
    @SerializedName("playGetChannelDetailsWithRecom")
    val channelDetails: ChannelDetail = ChannelDetail()
) {

    data class ChannelDetail(
        @SerializedName("meta")
        val meta: Meta = Meta(),

        @SerializedName("data")
        val dataList: List<Data> = emptyList()
    )

    data class Meta(
        @SerializedName("cursor")
        val cursor: String = ""
    )

    data class Data(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("cover_url")
        val coverUrl: String = "",

        @SerializedName("start_time")
        val startTime: String = "",

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
        val config: Config = Config(),

        @SerializedName("share")
        val share: Share = Share(),

        @SerializedName("air_time")
        val airTime: String = "",

        @SerializedName("description")
        val description: String = "",

        @SerializedName("performanceSummaryPageLink")
        val performanceSummaryPageLink: String = "",
    )

    data class Partner(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("type")
        val type: String = "seller", // [tokopedia, buyer, seller]

        @SerializedName("thumbnail_url")
        val thumbnailUrl: String = "",

        @SerializedName("badge_url")
        val badgeUrl: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("app_link")
        val appLink: String = "",
    )

    data class Video(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("orientation")
        val orientation: String = "vertical", // [vertical, horizontal]

        @SerializedName("type")
        val type: String = "vod", // [youtube, live, vod]

        @SerializedName("stream_source")
        val streamSource: String = "",

        @SerializedName("autoplay")
        val autoPlay: Boolean = true,

        @SerializedName("buffer_control")
        val bufferControl: BufferControl? = BufferControl(),
    )

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

    data class PinnedMessage(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("redirect_url")
        val redirectUrl: String = ""
    )

    data class Config(
        @SerializedName("welcome_format")
        val welcomeFormat: RealTimeNotification = RealTimeNotification(),

        @SerializedName("real_time_notif")
        val realTimeNotif: RealTimeNotificationConfig = RealTimeNotificationConfig(),

        @SerializedName("show_cart")
        val showCart: Boolean = false,

        @SerializedName("show_pinned_product")
        val showPinnedProduct: Boolean = false,

        @SerializedName("has_promo")
        val hasPromo: Boolean = false,

        @SerializedName("channel_freeze_screen")
        val freezeData: FreezeData = FreezeData(),

        @SerializedName("channel_banned_message")
        val bannedData: BannedData = BannedData(),

        @SerializedName("chat_config")
        val chatConfig: ChatConfig = ChatConfig(),

        @SerializedName("feeds_like_params")
        val feedLikeParam: FeedLikeParam = FeedLikeParam(),

        @SerializedName("room_background")
        val roomBackground: RoomBackground = RoomBackground(),

        @SerializedName("reminder")
        val reminder: Reminder = Reminder(),

        @SerializedName("multiple_like")
        val multipleLikeConfig: List<MultipleLikeConfig> = emptyList(),

        @SerializedName("has_follow_button")
        val hasFollowButton: Boolean = false,

        @SerializedName("empty_bottom_sheet")
        val emptyBottomSheet: EmptyBottomSheet = EmptyBottomSheet(),

        @SerializedName("status")
        val status: String = "",

        @SerializedName("channel_archived_screen")
        val archiveConfig: ArchivedData = ArchivedData(),

        @SerializedName("pop_up")
        val popupConfig: PopupConfig = PopupConfig(),

        @SerializedName("explore_widget")
        val exploreWidgetConfig: ExploreWidgetConfig = ExploreWidgetConfig(),
    )

    data class FreezeData(
        @SerializedName("title")
        val title: String = "",

        @SerializedName("description")
        val desc: String = "",

        @SerializedName("button_text")
        val buttonText: String = "",

        @SerializedName("button_app_link")
        val buttonAppLink: String = ""
    )

    data class BannedData(
        @SerializedName("title")
        val title: String = "",

        @SerializedName("message")
        val message: String = "",

        @SerializedName("button_text")
        val buttonText: String = ""
    )

    data class ChatConfig(
        @SerializedName("chat_enabled")
        val chatEnabled: Boolean = true,

        @SerializedName("chat_disabled_message")
        val chatDisabledMessage: String = ""
    )

    data class FeedLikeParam(
        @SerializedName("content_type")
        val contentType: Int = 0,

        @SerializedName("content_id")
        val contentId: String = "",

        @SerializedName("like_type")
        val likeType: Int = 1
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

        @SerializedName("meta_title")
        val metaTitle: String = "",

        @SerializedName("meta_description")
        val metaDescription: String = "",

        @SerializedName("is_show_button")
        val isShowButton: Boolean = false,
    )

    data class RealTimeNotificationConfig(
        @SerializedName("lifespan")
        val lifespan: Long = 0L,
    )

    data class Reminder(
        @SerializedName("is_set")
        val isSet: Boolean = false
    )

    data class EmptyBottomSheet(
        @SerializedName("copy_text_header")
        val headerText: String = "",

        @SerializedName("copy_text_body")
        val bodyText: String = "",

        @SerializedName("copy_text_redirect_button")
        val redirectButtonText: String = "",

        @SerializedName("image_url")
        val imageUrl: String = "",
    )

    data class ExploreWidgetConfig(
        @SerializedName("group")
        val group: String = "",

        @SerializedName("source_id")
        val sourceId: String = "",

        @SerializedName("source_type")
        val sourceType: String = "",
    )

    data class ArchivedData(
        @SerializedName("title")
        val title: String = "",

        @SerializedName("description")
        val description: String = "",

        @SerializedName("button_text")
        val buttonText: String = "",

        @SerializedName("button_app_link")
        val appLink: String = "",
    )

    data class PopupConfig(
        @SerializedName("is_enabled")
        val isEnabled: Boolean = false,

        @SerializedName("duration_to_pop_up")
        val duration: Long = 0,

        @SerializedName("copy_text_bottomsheet")
        val copyText: String = "",
    )
}
