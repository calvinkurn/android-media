package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import com.tokopedia.logisticcart.shipping.model.ShipmentData
import com.tokopedia.purchase_platform.common.feature.button.ABTestButton
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 2019-05-06.
 */

@Parcelize
class ShipmentButtonPaymentModel(
        var totalPrice: String = "-",
        var isCod: Boolean = false,
        var quantity: Int = 0,
        var abTestButton: ABTestButton = ABTestButton()
) : ShipmentData, Parcelable