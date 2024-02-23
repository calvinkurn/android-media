package com.tokopedia.shareexperience.data.dto

import com.google.gson.annotations.SerializedName

data class ShareExWrapperResponseDto(
    @SerializedName("getShareProperties")
    val response: ShareExSharePropertiesResponseDto = ShareExSharePropertiesResponseDto()
)
