package com.tokopedia.shareexperience.data.dto.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateLinkWrapperResponseDto(
    @SerializedName("generateAffiliateLinkEligibility")
    val affiliateLinkEligibilityResponseDto: ShareExAffiliateLinkEligibilityResponseDto = ShareExAffiliateLinkEligibilityResponseDto()
)
