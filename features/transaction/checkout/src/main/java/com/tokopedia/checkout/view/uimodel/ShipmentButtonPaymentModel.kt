package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 2019-05-06.
 */

@Parcelize
data class ShipmentButtonPaymentModel(
    val enable: Boolean = false,
    val totalPrice: String = "-",
    val quantity: Int = 0,
    var loading: Boolean = false
) : Parcelable
