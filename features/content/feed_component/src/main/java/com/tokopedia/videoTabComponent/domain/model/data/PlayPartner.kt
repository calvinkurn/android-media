package com.tokopedia.play.widget.sample.data

import com.google.gson.annotations.SerializedName

data class PlayPartner(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String = "",
    @SerializedName("badge_url")
    val badgeUrl: String = "",
    @SerializedName("app_link")
    var appLink: String = "",
    @SerializedName("web_link")
    val webLink: String = "",

    @SerializedName("next_cursor")
    var next_cursor: String = "",
    @SerializedName("is_autoplay")
    var is_autoplay: Boolean = false,
    @SerializedName("max_autoplay_in_cell")
    var max_autoplay_in_cell: Int = 0
)
