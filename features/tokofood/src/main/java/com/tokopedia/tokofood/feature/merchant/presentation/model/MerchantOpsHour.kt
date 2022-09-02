package com.tokopedia.tokofood.feature.merchant.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MerchantOpsHour(
        val initial: Char? = null,
        val day: String = "",
        val time: String = "",
        val isWarning: Boolean = false,
        val isToday: Boolean = false
) : Parcelable