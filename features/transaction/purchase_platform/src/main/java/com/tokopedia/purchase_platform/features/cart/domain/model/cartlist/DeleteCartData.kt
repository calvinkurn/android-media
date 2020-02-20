package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 20/02/18.
 */

data class DeleteCartData(
        var isSuccess: Boolean = false,
        var message: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isSuccess) 1 else 0)
        parcel.writeString(message)
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
