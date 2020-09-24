package com.tokopedia.checkout.view.uimodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.logisticcart.shipping.model.ShipmentData
import com.tokopedia.purchase_platform.common.feature.button.ABTestButton

/**
 * Created by Irfan Khoirul on 2019-05-06.
 */

class ShipmentButtonPaymentModel(
        var totalPrice: String = "-",
        var isCod: Boolean = false,
        var quantity: Int = 0,
        var abTestButton: ABTestButton = ABTestButton()
) : ShipmentData, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "-",
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readParcelable(ABTestButton::class.java.classLoader) ?: ABTestButton())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(totalPrice)
        parcel.writeByte(if (isCod) 1 else 0)
        parcel.writeInt(quantity)
        parcel.writeParcelable(abTestButton, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShipmentButtonPaymentModel> {
        override fun createFromParcel(parcel: Parcel): ShipmentButtonPaymentModel {
            return ShipmentButtonPaymentModel(parcel)
        }

        override fun newArray(size: Int): Array<ShipmentButtonPaymentModel?> {
            return arrayOfNulls(size)
        }
    }

}