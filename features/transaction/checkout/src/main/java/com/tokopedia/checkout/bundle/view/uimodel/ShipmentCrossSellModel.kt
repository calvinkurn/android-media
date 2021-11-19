package com.tokopedia.checkout.bundle.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentCrossSellModel(
        var crossSellModel: CrossSellModel = CrossSellModel(),
        var isChecked: Boolean = false,
        var isEnabled: Boolean = true
) : Parcelable
