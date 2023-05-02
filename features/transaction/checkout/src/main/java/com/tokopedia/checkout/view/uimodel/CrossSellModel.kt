package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrossSellModel(
    var id: String = "",
    var checkboxDisabled: Boolean = false,
    var isChecked: Boolean = false,
    var price: Double = 0.0,
    var info: CrossSellInfoModel = CrossSellInfoModel(),
    var orderSummary: CrossSellOrderSummaryModel = CrossSellOrderSummaryModel(),
    var bottomSheet: CrossSellBottomSheetModel = CrossSellBottomSheetModel(),
    var additionalVerticalId: String = "",
    var transactionType: String = ""
) : Parcelable
