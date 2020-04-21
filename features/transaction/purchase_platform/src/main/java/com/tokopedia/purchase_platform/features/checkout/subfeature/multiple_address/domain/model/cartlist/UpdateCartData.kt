package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 20/02/18.
 */

data class UpdateCartData(
        var isSuccess: Boolean = false,
        var message: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isSuccess) 1 else 0)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UpdateCartData> {
        override fun createFromParcel(parcel: Parcel): UpdateCartData {
            return UpdateCartData(parcel)
        }

        override fun newArray(size: Int): Array<UpdateCartData?> {
            return arrayOfNulls(size)
        }
    }
}
