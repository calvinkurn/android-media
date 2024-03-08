package com.tokopedia.shareexperience.data.dto.affiliate.generatelink

import com.google.gson.annotations.SerializedName

data class ShareExGenerateAffiliateLinkWrapperResponseDto(
    @SerializedName("generateAffiliateLink")
    val generateAffiliateLink: ShareExGenerateAffiliateLinkResponseDto
)
