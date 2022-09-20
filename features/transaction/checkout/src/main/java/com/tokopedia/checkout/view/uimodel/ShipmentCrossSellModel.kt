package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentCrossSellModel(
        var crossSellModel: CrossSellModel = CrossSellModel(),
        var isChecked: Boolean = false,
        var isEnabled: Boolean = true,
        var index: Int = -1,
) : Parcelable
