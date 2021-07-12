package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SummariesItemUiModel(
        var amount: Int = -1,
        var sectionName: String = "",
        var description: String = "",
        var details: List<DetailsItemUiModel> = listOf(),
        var sectionDescription: String = "",
        var type: String = "",
        var amountStr: String = ""
) : Parcelable