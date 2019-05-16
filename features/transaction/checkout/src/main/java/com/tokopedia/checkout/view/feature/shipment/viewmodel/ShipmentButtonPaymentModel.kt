package com.tokopedia.checkout.view.feature.shipment.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentData

/**
 * Created by Irfan Khoirul on 2019-05-06.
 */

class ShipmentButtonPaymentModel(
        var totalPrice: String = "-",
        var isCod: Boolean = false,
        var quantity: Int = 0
) : ShipmentData, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "-",
            parcel.readByte() != 0.toByte(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(totalPrice)
        parcel.writeByte(if (isCod) 1 else 0)
        parcel.writeInt(quantity)
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