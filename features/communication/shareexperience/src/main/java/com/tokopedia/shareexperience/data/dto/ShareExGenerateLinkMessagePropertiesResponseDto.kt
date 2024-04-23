package com.tokopedia.shareexperience.data.dto

import com.google.gson.annotations.SerializedName

data class ShareExGenerateLinkMessagePropertiesResponseDto(
    @SerializedName("message")
    val message: String = "",

    @SerializedName("placeholders")
    val placeholders: List<String> = listOf()
)
