package com.tokopedia.pdpsimulation.activateCheckout.domain.model

import android.os.Parcelable
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TenureSelectedModel(
    var priceText: String? = null,
    var tenure: String? = null,
    var installmentDetails: InstallmentDetails?
) : Parcelable
