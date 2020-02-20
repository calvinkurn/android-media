package com.tokopedia.purchase_platform.features.cart.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartTickerErrorData

/**
 * @author anggaprasetiyo on 02/03/18.
 */

data class CartItemTickerErrorHolderData(
        var cartTickerErrorData: CartTickerErrorData? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(CartTickerErrorData::class.java.classLoader) ?: null) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(cartTickerErrorData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItemTickerErrorHolderData> {
        override fun createFromParcel(parcel: Parcel): CartItemTickerErrorHolderData {
            return CartItemTickerErrorHolderData(parcel)
        }

        override fun newArray(size: Int): Array<CartItemTickerErrorHolderData?> {
            return arrayOfNulls(size)
        }
    }

}
