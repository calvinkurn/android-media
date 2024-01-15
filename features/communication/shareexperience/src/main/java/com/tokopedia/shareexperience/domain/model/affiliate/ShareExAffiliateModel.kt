package com.tokopedia.shareexperience.domain.model.affiliate

data class ShareExAffiliateModel(
    val registration: ShareExAffiliateRegistrationModel? = null,
    val eligibility: ShareExAffiliateEligibilityModel = ShareExAffiliateEligibilityModel()
)
