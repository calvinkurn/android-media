package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CaptureKycDocumentsParam(
    val projectId: String = "",
    val source: String = ""
): Parcelable
