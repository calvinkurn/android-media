package com.tokopedia.kyc_centralized.ui.gotoKyc.main.router

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParamFieldAnnotation")
@Parcelize
data class GotoKycMainParam (
    val projectId: String = "",
    val gotoKycType: String = "",
    val encryptedName: String = "",
    val isAccountLinked: Boolean = false,
    val isKtpTaken: Boolean = false,
    val isSelfieTaken: Boolean = false,
    val status: String = "",
    val sourcePage: String = "",
    val rejectionReason: String = "",
    val challengeId: String = "",
    val waitMessage: String = "",
    val directShowBottomSheet: Boolean = false,
    val callback: String = ""
): Parcelable
