package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParamFieldAnnotation")
@Parcelize
data class GotoKycMainParam (
    val projectId: String = "",
    val isCameFromAccountPage: Boolean = false,
    val dataSource: String = "",
    val status: String = "",
    val sourcePage: String = "",
    val listReason: List<String> = emptyList()
): Parcelable
