package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by alvarisi on 3/29/18.
 */

class CartAutoApplyVoucher(
        var isSuccess: Boolean = false,
        var code: String? = null,
        var isCoupon: Int = 0,
        var discountAmount: Long = 0,
        var title: String? = null,
        var messageSuccess: String? = null,
        var promoId: Long = 0
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isSuccess) 1 else 0)
        parcel.writeString(code)
        parcel.writeInt(isCoupon)
        parcel.writeLong(discountAmount)
        parcel.writeString(title)
        parcel.writeString(messageSuccess)
        parcel.writeLong(promoId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartAutoApplyVoucher> {
        override fun createFromParcel(parcel: Parcel): CartAutoApplyVoucher {
            return CartAutoApplyVoucher(parcel)
        }

        override fun newArray(size: Int): Array<CartAutoApplyVoucher?> {
            return arrayOfNulls(size)
        }
    }
}
