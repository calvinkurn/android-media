package com.tokopedia.shareexperience.data.dto.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateLinkEligibilityResponseDto(
    @SerializedName("EligibleCommission")
    val eligibleCommission: ShareExAffiliateEligibleCommissionResponseDto = ShareExAffiliateEligibleCommissionResponseDto()
)
