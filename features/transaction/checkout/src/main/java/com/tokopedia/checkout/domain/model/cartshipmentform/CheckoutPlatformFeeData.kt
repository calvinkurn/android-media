package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutPlatformFeeData(
    val isEnable: Boolean = false,
    val errorWording: String = "",
    val profileCode: String = "",
    val additionalData: String = ""
) : Parcelable
