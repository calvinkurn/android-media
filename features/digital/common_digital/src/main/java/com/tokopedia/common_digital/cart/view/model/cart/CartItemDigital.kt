package com.tokopedia.common_digital.cart.view.model.cart

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by Nabilla Sabbaha on 2/28/2017.
 */
class CartItemDigital : Parcelable, Visitable<Void> {

    var label: String? = null

    var value: String? = null

    constructor(label: String?, value: String?) {
        this.label = label
        this.value = value
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.label)
        dest.writeString(this.value)
    }

    protected constructor(`in`: Parcel) {
        this.label = `in`.readString()
        this.value = `in`.readString()
    }

    override fun type(typeFactory: Void): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItemDigital> {
        override fun createFromParcel(source: Parcel): CartItemDigital {
            return CartItemDigital(source)
        }

        override fun newArray(size: Int): Array<CartItemDigital?> {
            return arrayOfNulls(size)
        }
    }
}
