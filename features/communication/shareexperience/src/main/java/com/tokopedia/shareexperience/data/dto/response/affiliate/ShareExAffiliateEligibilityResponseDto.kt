package com.tokopedia.shareexperience.data.dto.response.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateEligibilityResponseDto(
    @SerializedName("eligibile")
    val eligible: Boolean = false,
    @SerializedName("commission")
    val commission: String = "",
    // TODO: Ask BE for Komisi Extra label and date
    @SerializedName("label")
    val label: String = "",
    @SerializedName("date")
    val date: String = ""
)
