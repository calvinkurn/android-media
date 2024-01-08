package com.tokopedia.shareexperience.domain.model.affiliate

data class ShareExAffiliateModel(
    val registration: ShareExAffiliateRegistrationModel? = null,
    val isEligible: Boolean = false,
    val commission: String = "",
    val label: String = "",
    val expiredDate: String = ""
)
