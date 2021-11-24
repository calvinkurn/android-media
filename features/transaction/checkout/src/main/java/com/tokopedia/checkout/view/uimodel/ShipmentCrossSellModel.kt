package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentCrossSellModel(
        var crossSellModel: CrossSellModel = CrossSellModel(),
        var isChecked: Boolean = false,
        var isEnabled: Boolean = true
) : Parcelable
