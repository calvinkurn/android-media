package com.tokopedia.shareexperience.data.dto.response.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateLinkEligibilityResponseDto(
    @SerializedName("EligibleCommission")
    val eligibleCommission: ShareExAffiliateEligibleCommissionResponseDto = ShareExAffiliateEligibleCommissionResponseDto()
)
