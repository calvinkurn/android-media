package com.tokopedia.shareexperience.data.dto.response.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateEligibilityResponseDto(
    @SerializedName("eligibile")
    val eligible: Boolean = false,
    @SerializedName("commission")
    val commission: String = ""
)
