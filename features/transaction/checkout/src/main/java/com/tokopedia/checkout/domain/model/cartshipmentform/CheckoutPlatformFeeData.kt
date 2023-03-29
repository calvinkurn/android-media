package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutPlatformFeeData(
    val isPlatformFee: Boolean = false,
    val ticker: String = "",
    val gatewayCode: String = "",
    val profileCode: String = ""
) : Parcelable
