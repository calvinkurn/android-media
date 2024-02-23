package com.tokopedia.shareexperience.data.dto

import com.google.gson.annotations.SerializedName

data class ShareExChannelListItemResponseDto(
    @SerializedName("id")
    val channelId: Long = 0,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("imageResolution")
    val imageResolution: String = "", // Square, Horizontal, Vertical

    @SerializedName("platform")
    val platform: String = ""
)
