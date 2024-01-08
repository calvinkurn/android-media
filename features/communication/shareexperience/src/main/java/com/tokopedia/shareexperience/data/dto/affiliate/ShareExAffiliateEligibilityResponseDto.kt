package com.tokopedia.shareexperience.data.dto.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateEligibilityResponseDto(
    @SerializedName("commission")
    val commission: String = "",
    @SerializedName("badge")
    val badge: String = "",
    @SerializedName("ExpiredDateFormatted")
    val expiredDate: String = ""
)
