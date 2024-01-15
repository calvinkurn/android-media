package com.tokopedia.shareexperience.domain.model.affiliate

data class ShareExAffiliateEligibilityModel(
    val isEligible: Boolean = false,
    val message: String = "",
    val label: String = "",
    val expiredDate: String = ""
)
