package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TradeInInfoData(
    var isValidTradeIn: Boolean = false,
    var newDevicePrice: Long = 0,
    var newDevicePriceFmt: String = "",
    var oldDevicePrice: Long = 0,
    var oldDevicePriceFmt: String = "",
    var isDropOffEnable: Boolean = false,
    var deviceModel: String = "",
    var diagnosticId: String = ""
) : Parcelable
