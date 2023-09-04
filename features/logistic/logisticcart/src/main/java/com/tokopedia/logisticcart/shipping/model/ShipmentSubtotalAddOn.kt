package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentSubtotalAddOn(
        var wording: String = "",
        var type: Int = -1
): Parcelable
