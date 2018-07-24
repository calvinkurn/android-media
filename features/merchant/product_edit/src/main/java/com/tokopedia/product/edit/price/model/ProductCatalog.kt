package com.tokopedia.product.edit.price.model

import android.os.Parcel
import android.os.Parcelable

class ProductCatalog(): Parcelable{
    var catalogId: Int = 0
    var catalogName: String? = null
    var catalogImage: String? = null

    constructor(parcel: Parcel) : this() {
        catalogId = parcel.readInt()
        catalogName = parcel.readString()
        catalogImage = parcel.readString()
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