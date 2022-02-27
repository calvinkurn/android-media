package com.tokopedia.createpost.uprofile.model

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
)

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
)

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

    @SerializedName("web_link")
    val webLink: String,

    @SerializedName("stats")
    val stats: PlayPostContentItemStats
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


