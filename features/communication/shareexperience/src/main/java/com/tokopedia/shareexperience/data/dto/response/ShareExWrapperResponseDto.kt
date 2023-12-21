package com.tokopedia.shareexperience.data.dto.response

import com.google.gson.annotations.SerializedName

data class ShareExWrapperResponseDto(
    @SerializedName("getShareProperties")
    val response: ShareExSharePropertiesResponseDto = ShareExSharePropertiesResponseDto()
)
