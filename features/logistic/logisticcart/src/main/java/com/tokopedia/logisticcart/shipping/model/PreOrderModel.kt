package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PreOrderModel (
        val header: String,
        val label: String,
        val display: Boolean
) : RatesViewModelType, Parcelable