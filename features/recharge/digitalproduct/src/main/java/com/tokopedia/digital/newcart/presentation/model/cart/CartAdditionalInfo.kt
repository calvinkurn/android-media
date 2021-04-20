package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable

import java.util.ArrayList

/**
 * @author by Nabilla Sabbaha on 3/1/2017.
 */

class CartAdditionalInfo(
        var title: String? = null,

        var cartItemDigitalList: List<CartItemDigital>? = null
) : Parcelable, Visitable<Void> {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createTypedArrayList(CartItemDigital)) {
    }

    override fun type(typeFactory: Void): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(cartItemDigitalList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartAdditionalInfo> {
        override fun createFromParcel(parcel: Parcel): CartAdditionalInfo {
            return CartAdditionalInfo(parcel)
        }

        override fun newArray(size: Int): Array<CartAdditionalInfo?> {
            return arrayOfNulls(size)
        }
    }
}
