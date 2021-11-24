package com.tokopedia.checkout.bundle.domain.model.saveshipmentstate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SaveShipmentStateData(
    var isSuccess: Boolean = false,
    var error: String = "",
    var message: String = "",
) : Parcelable