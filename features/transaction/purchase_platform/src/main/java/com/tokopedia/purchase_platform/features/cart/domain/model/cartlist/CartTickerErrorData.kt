package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable

/**
 * @author anggaprasetiyo on 02/03/18.
 */

data class CartTickerErrorData(
        var errorInfo: String? = null,
        var actionInfo: String? = null,
        var errorCount: Int = 0
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is CartTickerErrorData) {
            val data = obj as CartTickerErrorData?
            return errorCount == data?.errorCount && errorInfo == data.errorInfo && actionInfo == data.actionInfo
        }
        return super.equals(obj)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(errorInfo)
        parcel.writeString(actionInfo)
        parcel.writeInt(errorCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartTickerErrorData> {
        override fun createFromParcel(parcel: Parcel): CartTickerErrorData {
            return CartTickerErrorData(parcel)
        }

        override fun newArray(size: Int): Array<CartTickerErrorData?> {
            return arrayOfNulls(size)
        }
    }

}
