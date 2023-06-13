package com.tokopedia.tkpd.flashsale.presentation.detail.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimelineStepModel(
    val title: String = "",
    val period: String = "",
    var isEnded: Boolean = false,
    val isActive: Boolean = false,
    val icon: Int,
) : Parcelable
