package com.tokopedia.play.data.detail.recom

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.gson.annotations.SerializedName

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
    )

    data class Partner(

            @SerializedName("id")
            val id: String = "",

            @SerializedName("type")
            val type: String = "seller", // [tokopedia, buyer, seller]

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

            @SerializedName("show_cart")
            val showCart: Boolean = false,

            @SerializedName("show_pinned_product")
            val showPinnedProduct: Boolean = false,

            @SerializedName("active")
            val active: Boolean = true,

            @SerializedName("freezed")
            val freezed: Boolean = false,

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

            @SerializedName("pinned_product_config")
            val pinnedProductConfig: PinnedProductConfig = PinnedProductConfig(),

            @SerializedName("room_background")
            val roomBackground: RoomBackground = RoomBackground()
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

    data class PinnedProductConfig(

            @SerializedName("pin_title")
            val pinTitle: String = "",
            @SerializedName("bottom_sheet_title")
            val bottomSheetTitle: String = ""
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

            @SerializedName("is_show_button")
            val isShowButton: Boolean = false
    )
}