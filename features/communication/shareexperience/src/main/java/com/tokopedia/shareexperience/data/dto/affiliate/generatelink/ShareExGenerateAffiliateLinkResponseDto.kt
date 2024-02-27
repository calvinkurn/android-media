package com.tokopedia.shareexperience.data.dto.affiliate.generatelink

import com.google.gson.annotations.SerializedName

data class ShareExGenerateAffiliateLinkResponseDto(
    @SerializedName("Data")
    val data: List<ShareExGenerateAffiliateLinkDataResponseDto> = listOf()
)
