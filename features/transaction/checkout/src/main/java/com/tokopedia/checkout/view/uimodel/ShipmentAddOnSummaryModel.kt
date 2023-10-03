package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentAddOnSummaryModel(
    var wording: String = "",
    var type: Int = -1,
    var qty: Int = -1,
    var priceLabel: String = "",
    var priceValue: Long = 0L
) : Parcelable
