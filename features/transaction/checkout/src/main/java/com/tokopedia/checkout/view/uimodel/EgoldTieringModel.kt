package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EgoldTieringModel(
        var minTotalAmount: Long = 0,
        var minAmount: Long = 0,
        var maxAmount: Long = 0,
        var basisAmount: Long = 0
) : Parcelable