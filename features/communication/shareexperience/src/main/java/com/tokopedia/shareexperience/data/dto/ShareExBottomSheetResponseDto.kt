package com.tokopedia.shareexperience.data.dto

import com.google.gson.annotations.SerializedName

data class ShareExBottomSheetResponseDto(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("properties")
    val properties: List<ShareExPropertyResponseDto> = listOf()
)
