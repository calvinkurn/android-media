package com.tokopedia.tokofood.feature.merchant.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetail(
        val qty: Int = 0
) : Parcelable
