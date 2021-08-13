package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 2019-05-06.
 */

@Parcelize
class ShipmentButtonPaymentModel(
        var totalPrice: String = "-",
        var quantity: Int = 0,
) : Parcelable