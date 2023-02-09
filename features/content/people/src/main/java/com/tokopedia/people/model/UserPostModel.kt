package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class UserPostModel(
    @SerializedName("playGetContentSlot")
    val playGetContentSlot: PlayGetContentSlot = PlayGetContentSlot(),
)

data class PlayGetContentSlot(
    @SerializedName("data")
    val data: List<PlayPostContent> = emptyList(),
    @SerializedName("meta")
    val playGetContentSlot: PlayGetContentSlotMeta = PlayGetContentSlotMeta(),
) : BaseItem()

data class PlayGetContentSlotMeta(
    @SerializedName("is_autoplay")
    val isAutoplay: Boolean = false,
    @SerializedName("max_autoplay_in_cell")
    val maxAutoplayInCell: Int = 0,
    @SerializedName("next_cursor")
    val nextCursor: String = "",
)

data class PlayPostContent(
    @SerializedName("hash")
    val hash: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("items")
    val items: List<PlayPostContentItem> = emptyList(),
) : BaseItem()

data class PlayPostContentItem(
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
    val stats: PlayPostContentItemStats = PlayPostContentItemStats(),
    @SerializedName("configurations")
    val configurations: PlayPostConfigurations = PlayPostConfigurations(),
    @SerializedName("partner")
    val partner: Partner = Partner(),
    @SerializedName("display_type")
    val displayType: String = "",
) : BaseItem()

data class Partner(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
)

data class PlayPostContentItemStats(
    @SerializedName("view")
    val view: StatsView = StatsView(),
)

data class StatsView(
    @SerializedName("value")
    var value: String = "",
    @SerializedName("formatted")
    var formatted: String = "",
)

data class PlayPostConfigurations(
    @SerializedName("has_promo")
    val hasPromo: Boolean = false,
    @SerializedName("reminder")
    val reminder: PostReminder = PostReminder(),
    @SerializedName("promo_labels")
    val promoLabels: List<PostPromoLabel> = emptyList(),
)

data class PostReminder(
    @SerializedName("is_set")
    var isSet: Boolean = false,
)

data class PostPromoLabel(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("type")
    val type: String = "",
)
