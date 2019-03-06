package com.tokopedia.common_digital.cart.view.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by Nabilla Sabbaha on 3/1/2017.
 */

class UserInputPriceDigital : Parcelable {

    var minPayment: String? = null

    var maxPayment: String? = null

    var minPaymentPlain: Long = 0

    var maxPaymentPlain: Long = 0


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.minPayment)
        dest.writeString(this.maxPayment)
        dest.writeLong(this.minPaymentPlain)
        dest.writeLong(this.maxPaymentPlain)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.minPayment = `in`.readString()
        this.maxPayment = `in`.readString()
        this.minPaymentPlain = `in`.readLong()
        this.maxPaymentPlain = `in`.readLong()
    }

    companion object CREATOR : Parcelable.Creator<UserInputPriceDigital> {
        override fun createFromParcel(source: Parcel): UserInputPriceDigital {
            return UserInputPriceDigital(source)
        }

        override fun newArray(size: Int): Array<UserInputPriceDigital?> {
            return arrayOfNulls(size)
        }
    }
}
