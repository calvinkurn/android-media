package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class UserPostModel(
    @SerializedName("playGetContentSlot")
    val playGetContentSlot: PlayGetContentSlot
)


data class PlayGetContentSlot(
    @SerializedName("data")
    val data: MutableList<PlayPostContent>,

    @SerializedName("meta")
    val playGetContentSlot: PlayGetContentSlotMeta
): BaseItem()

data class PlayGetContentSlotMeta(
    @SerializedName("is_autoplay")
    val isAutoplay: Boolean,

    @SerializedName("max_autoplay_in_cell")
    val maxAutoplayInCell: Int,

    @SerializedName("next_cursor")
    val nextCursor: String
)

data class PlayPostContent(
    @SerializedName("hash")
    val hash: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("items")
    val items: MutableList<PlayPostContentItem>
): BaseItem()

data class PlayPostContentItem(
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
    val stats: PlayPostContentItemStats,

    @SerializedName("configurations")
    val configurations: PlayPostConfigurations
) : BaseItem()

data class PlayPostContentItemStats(
    @SerializedName("view")
    val view: StatsView
)

data class StatsView(
    @SerializedName("value")
    val value: String,

    @SerializedName("formatted")
    val formatted: String
)

data class PlayPostConfigurations(
    @SerializedName("has_promo")
    val hasPromo: Boolean,

    @SerializedName("reminder")
    val reminder: PostReminder,

    @SerializedName("promo_labels")
    val promoLabels: List<PostPromoLabel>
)

data class PostReminder(
    @SerializedName("is_set")
    var isSet: Boolean
)

data class PostPromoLabel(
    @SerializedName("text")
    val text: String,
    val type: String
)


