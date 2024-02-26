package com.tokopedia.shareexperience.data.dto

import com.google.gson.annotations.SerializedName

data class ShareExChannelResponseDto(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("list")
    val list: List<ShareExChannelListItemResponseDto> = listOf()
)
