package com.tokopedia.videoTabComponent.domain.model.data

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
    val webLink: String = ""
)
