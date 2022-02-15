package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnBottomSheetModel(
        var ticker: AddOnTickerModel? = null,
        var headerTitle: String = "",
        var description: String = "",
        var products: List<AddOnProductItemModel>? = emptyList()
): Parcelable
