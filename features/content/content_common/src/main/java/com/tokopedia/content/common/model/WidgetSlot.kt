package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/11/22
 */
data class WidgetSlot(
    @SerializedName("playGetContentSlot")
    val playGetContentSlot: ContentSlot = ContentSlot(),
)

data class ContentSlot(
    @SerializedName("data")
    val data: List<Content> = emptyList(),

    @SerializedName("meta")
    val playGetContentSlot: ContentSlotMeta = ContentSlotMeta(),
)

data class ContentSlotMeta(
    @SerializedName("is_autoplay")
    val isAutoplay: Boolean = false,

    @SerializedName("max_autoplay_in_cell")
    val maxAutoplayInCell: Int = 0,

    @SerializedName("next_cursor")
    val nextCursor: String = ""
)

data class Content(
    @SerializedName("hash")
    val hash: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("id")
    val id: String = "",

    @SerializedName("items")
    val items: List<ContentItem> = emptyList(),
)

data class ContentItem(
    @SerializedName("app_link")
    val appLink: String = "",

    @SerializedName("cover_url")
    val coverUrl: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("is_live")
    val isLive: Boolean = false,

    @SerializedName("id")
    val id: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("start_time")
    val startTime: String = "",

    @SerializedName("air_time")
    val airTime: String = "",

    @SerializedName("web_link")
    val webLink: String = "",

    @SerializedName("stats")
    val stats: ContentStats = ContentStats(),

    @SerializedName("configurations")
    val configurations: ContentItemConfiguration = ContentItemConfiguration(),

    @SerializedName("partner")
    val partner: Partner = Partner(),

    @SerializedName("video")
    val video: Video = Video(),

    // Banner
    @SerializedName("created_time")
    val createdTime: String = "",

    @SerializedName("updated_time")
    val updatedTime: String = "",

    @SerializedName("slug")
    val slug: String = "",

    @SerializedName("broadcaster_name")
    val broadcaster_name: String = "",

    @SerializedName("image_url")
    val imageUrl: String = "",

    // Tab
    @SerializedName("label")
    val label: String = "",

    @SerializedName("icon_url")
    val iconUrl: String = "",

    @SerializedName("group")
    val group: String = "",

    @SerializedName("source_type")
    val sourceType: String = "",

    @SerializedName("source_id")
    val sourceId: String = "",

    @SerializedName("slug_id")
    val slugId: String = "",

    @SerializedName("recommendationType")
    val recommendationType: String = "",
) {

    data class Partner(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("type")
        val type: String = "",

        @SerializedName("thumbnail_url")
        val thumbnailUrl: String = "",

        @SerializedName("badge_url")
        val badgeUrl: String = "",

        @SerializedName("app_link")
        val appLink: String = "",
    )

    data class Video(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("stream_source")
        val streamUrl: String = "",

        @SerializedName("orientation")
        val orientation: String = "",

        @SerializedName("type")
        val type: String = "",
    )
}

data class ContentStats(
    @SerializedName("view")
    val view: StatsView = StatsView(),
)

data class StatsView(
    @SerializedName("value")
    val value: String = "",

    @SerializedName("formatted")
    val formatted: String = "",
)

data class ContentItemConfiguration(
    @SerializedName("has_promo")
    val hasPromo: Boolean = false,

    @SerializedName("reminder")
    val reminder: Reminder = Reminder(),

    @SerializedName("promo_labels")
    val promoLabels: List<PromoLabel> = emptyList(),
) {
    data class Reminder(
        @SerializedName("is_set")
        val isSet: Boolean = false,
    )

    data class PromoLabel(
        @SerializedName("text")
        val text: String = "",

        @SerializedName("type")
        val type: String = "",
    )
}
