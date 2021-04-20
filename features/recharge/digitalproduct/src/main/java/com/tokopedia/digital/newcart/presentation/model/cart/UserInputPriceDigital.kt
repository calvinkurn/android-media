package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by Nabilla Sabbaha on 3/1/2017.
 */

class UserInputPriceDigital(
        var minPayment: String? = null,

        var maxPayment: String? = null,

        var minPaymentPlain: Long = 0,

        var maxPaymentPlain: Long = 0
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(minPayment)
        parcel.writeString(maxPayment)
        parcel.writeLong(minPaymentPlain)
        parcel.writeLong(maxPaymentPlain)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserInputPriceDigital> {
        override fun createFromParcel(parcel: Parcel): UserInputPriceDigital {
            return UserInputPriceDigital(parcel)
        }

        override fun newArray(size: Int): Array<UserInputPriceDigital?> {
            return arrayOfNulls(size)
        }
    }

}
