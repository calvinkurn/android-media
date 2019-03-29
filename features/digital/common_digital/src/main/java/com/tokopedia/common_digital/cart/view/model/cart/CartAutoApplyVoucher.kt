package com.tokopedia.common_digital.cart.view.model.cart

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by alvarisi on 3/29/18.
 */

class CartAutoApplyVoucher : Parcelable {
    var isSuccess: Boolean = false
    var code: String? = null
    var isCoupon: Int = 0
    var discountAmount: Long = 0
    var title: String? = null
    var messageSuccess: String? = null
    var promoId: Long = 0

    constructor() {}

    protected constructor(`in`: Parcel) {
        isSuccess = `in`.readByte().toInt() != 0
        code = `in`.readString()
        isCoupon = `in`.readInt()
        discountAmount = `in`.readLong()
        title = `in`.readString()
        messageSuccess = `in`.readString()
        promoId = `in`.readLong()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeByte((if (isSuccess) 1 else 0).toByte())
        parcel.writeString(code)
        parcel.writeInt(isCoupon)
        parcel.writeLong(discountAmount)
        parcel.writeString(title)
        parcel.writeString(messageSuccess)
        parcel.writeLong(promoId)
    }

    companion object CREATOR : Parcelable.Creator<CartAutoApplyVoucher> {
        override fun createFromParcel(`in`: Parcel): CartAutoApplyVoucher {
            return CartAutoApplyVoucher(`in`)
        }

        override fun newArray(size: Int): Array<CartAutoApplyVoucher?> {
            return arrayOfNulls(size)
        }
    }
}
