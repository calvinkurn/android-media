package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TradeInInfoData(
        var isValidTradeIn: Boolean = false,
        var newDevicePrice: Int = 0,
        var newDevicePriceFmt: String = "",
        var oldDevicePrice: Int = 0,
        var oldDevicePriceFmt: String = "",
        var isDropOffEnable: Boolean = false,
        var deviceModel: String = "",
        var diagnosticId: String = "",
) : Parcelable