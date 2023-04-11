package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    val listReason: List<String> = emptyList(),
    val challengeId: String = "",
    val waitMessage: String = ""
): Parcelable
