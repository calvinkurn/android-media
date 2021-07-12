package com.tokopedia.checkout.domain.model.saveshipmentstate

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SaveShipmentStateData(
    var isSuccess: Boolean = false,
    var error: String = "",
    var message: String = "",
) : Parcelable