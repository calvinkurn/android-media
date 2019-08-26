package com.tokopedia.product.manage.item.stock.view.model

import android.os.Parcel
import android.os.Parcelable

data class ProductStock(var isActive: Boolean = true, var stockCount: Int = 0, var sku: String = "") : Parcelable {

    fun getStockInteger(): Int {
        if (isActive && stockCount == 0) {
            return STOCK_UNLIMITED
        } else if (isActive && stockCount > 0) {
            return STOCK_LIMITED
        } else if (!isActive) {
            return STOCK_DELETED
        }
        return STOCK_DEFAULT
    }

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeInt(stockCount)
        parcel.writeString(sku)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductStock> {

        const val STOCK_DEFAULT = 0
        const val STOCK_UNLIMITED = 1
        const val STOCK_LIMITED = 2
        const val STOCK_DELETED = 3

        override fun createFromParcel(parcel: Parcel): ProductStock {
            return ProductStock(parcel)
        }

        override fun newArray(size: Int): Array<ProductStock?> {
            return arrayOfNulls(size)
        }
    }

}