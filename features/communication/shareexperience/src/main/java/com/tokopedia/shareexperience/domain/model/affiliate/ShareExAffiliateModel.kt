package com.tokopedia.shareexperience.domain.model.affiliate

data class ShareExAffiliateModel(
    val registration: ShareExAffiliateRegistrationModel? = null,
    val commission: String = "",
    val label: String = "",
    val expiredDate: String = ""
)
