package com.tokopedia.common_digital.cart.view.model.cart

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable

import java.util.ArrayList

/**
 * @author by Nabilla Sabbaha on 3/1/2017.
 */

class CartAdditionalInfo : Parcelable, Visitable<Void> {

    var title: String? = null

    var cartItemDigitalList: List<CartItemDigital>? = null

    constructor(title: String?, cartItemDigitalList: List<CartItemDigital>) {
        this.title = title
        this.cartItemDigitalList = cartItemDigitalList
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.title)
        dest.writeList(this.cartItemDigitalList)
    }

    protected constructor(`in`: Parcel) {
        this.title = `in`.readString()
        this.cartItemDigitalList = ArrayList()
        `in`.readList(this.cartItemDigitalList, CartItemDigital::class.java.classLoader)
    }

    override fun type(typeFactory: Void): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartAdditionalInfo> {
        override fun createFromParcel(source: Parcel): CartAdditionalInfo {
            return CartAdditionalInfo(source)
        }

        override fun newArray(size: Int): Array<CartAdditionalInfo?> {
            return arrayOfNulls(size)
        }
    }
}
