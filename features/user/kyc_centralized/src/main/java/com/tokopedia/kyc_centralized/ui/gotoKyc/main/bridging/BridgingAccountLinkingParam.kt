package com.tokopedia.kyc_centralized.ui.gotoKyc.main.bridging

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParamFieldAnnotation")
@Parcelize
data class BridgingAccountLinkingParam(
    val projectId: String = "",
    val source: String = "",
    val callback: String = ""
): Parcelable
