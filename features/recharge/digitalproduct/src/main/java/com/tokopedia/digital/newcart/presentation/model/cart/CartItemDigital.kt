package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by Nabilla Sabbaha on 2/28/2017.
 */
class CartItemDigital(
        var label: String? = null,

        var value: String? = null
) : Parcelable, Visitable<Void> {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun type(typeFactory: Void): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(label)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItemDigital> {
        override fun createFromParcel(parcel: Parcel): CartItemDigital {
            return CartItemDigital(parcel)
        }

        override fun newArray(size: Int): Array<CartItemDigital?> {
            return arrayOfNulls(size)
        }
    }

}
