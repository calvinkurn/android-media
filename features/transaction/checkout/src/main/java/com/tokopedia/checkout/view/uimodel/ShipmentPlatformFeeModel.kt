package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentPlatformFeeModel(
        var title: String = "",
        var fee: Double = 0.0,
        var slashedFeeTitle: String = "",
        var slashedFee: Double = 0.0,
        var minRange: Double = 0.0,
        var maxRange: Double = 0.0,
        var isLoading: Boolean = false,
        var isShowTooltip: Boolean = false,
        var tooltip: String = "",
        var isShowTicker: Boolean = false,
        var ticker: String = ""
) : Parcelable
