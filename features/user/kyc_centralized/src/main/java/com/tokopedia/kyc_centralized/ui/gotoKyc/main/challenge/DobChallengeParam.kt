package com.tokopedia.kyc_centralized.ui.gotoKyc.main.challenge

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DobChallengeParam(
    val projectId: String = "",
    val challengeId: String = "",
    val pageSource: String = ""
): Parcelable
