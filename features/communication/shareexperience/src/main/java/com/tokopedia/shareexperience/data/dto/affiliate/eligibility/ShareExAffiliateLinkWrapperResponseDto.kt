package com.tokopedia.shareexperience.data.dto.affiliate.eligibility

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateLinkWrapperResponseDto(
    @SerializedName("generateAffiliateLinkEligibility")
    val affiliateLinkEligibilityResponseDto: ShareExAffiliateLinkEligibilityResponseDto = ShareExAffiliateLinkEligibilityResponseDto()
)
