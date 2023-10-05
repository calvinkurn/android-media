package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentAddOnSubtotalModel(
    var wording: String = "",
    var priceLabel: String = ""
) : Parcelable
