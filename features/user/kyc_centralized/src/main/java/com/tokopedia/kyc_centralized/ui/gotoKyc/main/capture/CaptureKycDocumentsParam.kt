package com.tokopedia.kyc_centralized.ui.gotoKyc.main.capture

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParamFieldAnnotation")
@Parcelize
data class CaptureKycDocumentsParam(
    val projectId: String = "",
    val source: String = "",
    val callback: String = ""
): Parcelable
