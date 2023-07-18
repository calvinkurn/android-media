package com.tokopedia.kyc_centralized.ui.gotoKyc.main.submit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FinalLoaderParam(
    val source: String = "",
    val projectId: String = "",
    val challengeId: String = "",
    val gotoKycType: String  = ""
) : Parcelable
