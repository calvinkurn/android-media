package com.tokopedia.kyc_centralized.ui.gotoKyc.main.bridgingAccountLinking

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BridgingAccountLinkingParam(
    val projectId: String = "",
    val source: String = ""
): Parcelable
