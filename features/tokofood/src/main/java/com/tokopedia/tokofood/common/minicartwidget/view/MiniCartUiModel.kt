package com.tokopedia.tokofood.common.minicartwidget.view

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MiniCartUiModel(
        var shopName: String = "",
        var totalPriceFmt: String = "",
        var totalProductQuantity: Int = 0
): Parcelable