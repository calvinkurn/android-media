package com.tokopedia.shareexperience.data.dto.affiliate.properties

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateEligibilityResponseDto(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("badge")
    val badge: String = "",
    @SerializedName("expiredDataFormatted")
    val expiredDate: String = ""
)
