package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentPlatformFeeData(
        var isEnable: Boolean = false,
        var errorWording: String = "",
        var profileCode: String = "",
        var additionalData: String = ""
) : Parcelable
