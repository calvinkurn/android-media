package com.tokopedia.shareexperience.data.dto

import com.google.gson.annotations.SerializedName

data class ShareExSharePropertiesResponseDto(
    @SerializedName("bottomsheet")
    val bottomSheet: ShareExBottomSheetResponseDto = ShareExBottomSheetResponseDto()
)
