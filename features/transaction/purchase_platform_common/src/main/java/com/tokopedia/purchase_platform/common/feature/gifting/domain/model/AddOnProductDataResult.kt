package com.tokopedia.purchase_platform.common.feature.gifting.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SaveAddOnStateResult(
    var addOns: List<AddOnResult> = emptyList()
) : Parcelable

@Parcelize
data class AddOnResult(
    var addOnBottomSheet: AddOnBottomSheetResult = AddOnBottomSheetResult(),
    var addOnButton: AddOnButtonResult = AddOnButtonResult(),
    var addOnData: List<AddOnData> = emptyList(),
    var addOnKey: String = "",
    var addOnLevel: String = "",
    var status: Int = 0
) : Parcelable

@Parcelize
data class AddOnBottomSheetResult(
    var description: String = "",
    var headerTitle: String = "",
    var products: List<ProductResult> = emptyList(),
    var ticker: TickerResult = TickerResult()
) : Parcelable

@Parcelize
data class AddOnButtonResult(
    var action: Int = 0,
    var description: String = "",
    var leftIconUrl: String = "",
    var rightIconUrl: String = "",
    var title: String = ""
) : Parcelable

@Parcelize
data class ProductResult(
    var productImageUrl: String = "",
    var productName: String = ""
) : Parcelable

@Parcelize
data class TickerResult(
    var text: String = ""
) : Parcelable
