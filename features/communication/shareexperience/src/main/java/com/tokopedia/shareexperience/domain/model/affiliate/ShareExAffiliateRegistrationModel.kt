package com.tokopedia.shareexperience.domain.model.affiliate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExAffiliateRegistrationModel(
    val icon: String = "",
    val title: String = "",
    val label: String = "",
    val description: String = "",
    val appLink: String = ""
): Parcelable
