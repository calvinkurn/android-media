package com.tokopedia.buyerorder.recharge.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RechargePaymentInfoMessage (
    val message: String = "",
    val urlText: String = "",
    val appLink: String = "",
): Parcelable
