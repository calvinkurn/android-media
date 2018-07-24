package com.tokopedia.product.edit.price.model

import android.os.Parcel
import android.os.Parcelable

class ProductName(): Parcelable{
    var name: String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductName> {
        override fun createFromParcel(parcel: Parcel): ProductName {
            return ProductName(parcel)
        }

        override fun newArray(size: Int): Array<ProductName?> {
            return arrayOfNulls(size)
        }
    }
}