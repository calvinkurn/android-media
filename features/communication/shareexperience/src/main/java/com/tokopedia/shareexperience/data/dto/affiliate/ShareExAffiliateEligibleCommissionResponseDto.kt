package com.tokopedia.shareexperience.data.dto.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateEligibleCommissionResponseDto(
    @SerializedName("IsEligible")
    val isEligible: Boolean = false
)
