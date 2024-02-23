package com.tokopedia.shareexperience.domain.model.affiliate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExAffiliateEligibilityModel(
    val isEligible: Boolean = false,
    val message: String = "",
    val label: String = "",
    val expiredDate: String = ""
): Parcelable
