package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 2019-05-06.
 */

@Parcelize
class ShipmentButtonPaymentModel(
        var enable: Boolean = false,
        var totalPrice: String = "-",
        var quantity: Int = 0,
) : Parcelable