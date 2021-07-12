package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailsItemUiModel(
        var amount: Int = -1,
        var sectionName: String = "",
        var description: String = "",
        var type: String = "",
        var amountStr: String = "",
        var points: Int = -1,
        var pointsStr: String = "",
        var currencyDetailString: String = ""
) : Parcelable
