package com.tokopedia.product.edit.price.model

import android.os.Parcel
import android.os.Parcelable

class ProductCategory(): Parcelable{
    var categoryId: Int = 0
    var categoryName: String? = null

    constructor(parcel: Parcel) : this() {
        categoryId = parcel.readInt()
        categoryName = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(categoryId)
        parcel.writeString(categoryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductCategory> {
        override fun createFromParcel(parcel: Parcel): ProductCategory {
            return ProductCategory(parcel)
        }

        override fun newArray(size: Int): Array<ProductCategory?> {
            return arrayOfNulls(size)
        }
    }
}