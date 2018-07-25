package com.tokopedia.product.edit.price.model

import android.os.Parcel
import android.os.Parcelable

class ProductStock(): Parcelable{
    var stockLimited: Boolean = false
    var stockCount: Int = 0
    var sku: String? = null

    constructor(parcel: Parcel) : this() {
        stockLimited = parcel.readByte() != 0.toByte()
        stockCount = parcel.readInt()
        sku = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (stockLimited) 1 else 0)
        parcel.writeInt(stockCount)
        parcel.writeString(sku)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductStock> {
        override fun createFromParcel(parcel: Parcel): ProductStock {
            return ProductStock(parcel)
        }

        override fun newArray(size: Int): Array<ProductStock?> {
            return arrayOfNulls(size)
        }
    }

}