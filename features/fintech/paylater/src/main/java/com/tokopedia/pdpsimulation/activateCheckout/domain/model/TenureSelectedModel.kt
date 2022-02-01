package com.tokopedia.pdpsimulation.activateCheckout.domain.model

import android.os.Parcelable
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TenureSelectedModel(
    val priceText:String? = null,
    val installmentDetails: InstallmentDetails?
): Parcelable
