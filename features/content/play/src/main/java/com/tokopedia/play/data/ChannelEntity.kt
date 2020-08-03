package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 03/08/20.
 */
data class ChannelEntity(
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
        @SerializedName("app_link")
        val appLink: String = "",
        @SerializedName("web")
        val web: String = ""
) {

    data class Data(
            @SerializedName("data")
            val channel: ChannelEntity = ChannelEntity()
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
            @SerializedName("thumbnailURL")
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
            val hasPromo: Boolean = false)
}