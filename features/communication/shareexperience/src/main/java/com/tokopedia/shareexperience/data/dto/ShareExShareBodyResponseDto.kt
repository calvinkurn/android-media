package com.tokopedia.shareexperience.data.dto

import com.google.gson.annotations.SerializedName

data class ShareExShareBodyResponseDto(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("thumbnailUrls")
    val thumbnailUrls: List<String> = listOf()
)
