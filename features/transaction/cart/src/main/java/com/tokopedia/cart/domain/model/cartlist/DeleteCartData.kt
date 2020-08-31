package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 20/02/18.
 */

data class DeleteCartData(
        var isSuccess: Boolean = false,
        var message: String? = null,
        var cartCounter: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isSuccess) 1 else 0)
        parcel.writeString(message)
        parcel.writeInt(cartCounter)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DeleteCartData> {
        override fun createFromParcel(parcel: Parcel): DeleteCartData {
            return DeleteCartData(parcel)
        }

        override fun newArray(size: Int): Array<DeleteCartData?> {
            return arrayOfNulls(size)
        }
    }
}