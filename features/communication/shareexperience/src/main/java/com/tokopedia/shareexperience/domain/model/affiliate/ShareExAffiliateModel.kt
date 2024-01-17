package com.tokopedia.shareexperience.domain.model.affiliate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExAffiliateModel(
    val registration: ShareExAffiliateRegistrationModel? = null,
    val eligibility: ShareExAffiliateEligibilityModel = ShareExAffiliateEligibilityModel()
): Parcelable
