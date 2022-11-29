package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/11/22
 */
data class WidgetSlot(
    @SerializedName("playGetContentSlot")
    val playGetContentSlot: ContentSlot,
)

data class ContentSlot(
    @SerializedName("data")
    val data: MutableList<Content>,

    @SerializedName("meta")
    val playGetContentSlot: ContentSlotMeta,
)

data class ContentSlotMeta(
    @SerializedName("is_autoplay")
    val isAutoplay: Boolean,

    @SerializedName("max_autoplay_in_cell")
    val maxAutoplayInCell: Int,

    @SerializedName("next_cursor")
    val nextCursor: String,
)

data class Content(
    @SerializedName("hash")
    val hash: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("items")
    val items: MutableList<ContentItem>,
)

data class ContentItem(
    @SerializedName("app_link")
    val appLink: String,

    @SerializedName("cover_url")
    val coverUrl: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("is_live")
    val isLive: Boolean,

    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("air_time")
    val airTime: String,

    @SerializedName("web_link")
    val webLink: String,

    @SerializedName("stats")
    val stats: ContentStats,

    @SerializedName("configurations")
    val configurations: ContentItemConfiguration,

    @SerializedName("partner")
    val partner: Partner,
) {

    data class Partner(
        @SerializedName("id")
        val id: String,

        @SerializedName("name")
        val name: String,
    )
}

data class ContentStats(
    @SerializedName("view")
    val view: StatsView,
)

data class StatsView(
    @SerializedName("value")
    val value: String,

    @SerializedName("formatted")
    val formatted: String,
)

data class ContentItemConfiguration(
    @SerializedName("has_promo")
    val hasPromo: Boolean,

    @SerializedName("reminder")
    val reminder: Reminder,

    @SerializedName("promo_labels")
    val promoLabels: List<PromoLabel>,
) {
    data class Reminder(
        @SerializedName("is_set")
        val isSet: Boolean,
    )

    data class PromoLabel(
        @SerializedName("text")
        val text: String,
        @SerializedName("type")
        val type: String,
    )
}

