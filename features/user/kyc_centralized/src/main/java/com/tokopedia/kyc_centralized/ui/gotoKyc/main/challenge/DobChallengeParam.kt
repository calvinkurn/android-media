package com.tokopedia.kyc_centralized.ui.gotoKyc.main.challenge

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParamFieldAnnotation")
@Parcelize
data class DobChallengeParam(
    val projectId: String = "",
    val challengeId: String = "",
    val pageSource: String = "",
    val callback: String = ""
): Parcelable
