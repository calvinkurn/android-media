package com.tokopedia.kyc_centralized.ui.gotoKyc.main.onboard

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardBenefitParam(
    val projectId: String = "",
    val gotoKycType: String = "",
    val encryptedName: String = "",
    val isAccountLinked: Boolean = false,
    val isKtpTaken: Boolean = false,
    val isSelfieTaken: Boolean = false,
    val sourcePage: String = "",
    val directShowBottomSheet: Boolean = false
): Parcelable
