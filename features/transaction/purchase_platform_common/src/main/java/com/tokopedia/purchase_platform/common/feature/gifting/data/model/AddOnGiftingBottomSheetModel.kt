package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnGiftingBottomSheetModel(
    var ticker: AddOnGiftingTickerModel = AddOnGiftingTickerModel(),
    var headerTitle: String = "",
    var description: String = "",
    var products: List<AddOnGiftingProductItemModel> = emptyList()
) : Parcelable
