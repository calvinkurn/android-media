package com.tokopedia.product.edit.price.model

import android.os.Parcel
import android.os.Parcelable

data class ProductCatalog(var catalogId : Int = 0, var catalogName : String = "", var catalogImage : String = ""): Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(catalogId)
        parcel.writeString(catalogName)
        parcel.writeString(catalogImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductCatalog> {
        override fun createFromParcel(parcel: Parcel): ProductCatalog {
            return ProductCatalog(parcel)
        }

        override fun newArray(size: Int): Array<ProductCatalog?> {
            return arrayOfNulls(size)
        }
    }
}