package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentSubtotalAddOnData(
    val wording: String = "",
    val type: Int = -1
) : Parcelable
