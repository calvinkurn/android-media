package com.tokopedia.expresscheckout.view.variant.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 13/12/18.
 */

data class ProductChild(
        var productId: Int,
        var productName: String,
        var productPrice: Int,
        var productImageUrl: String,
        var isAvailable: Boolean,
        var isSelected: Boolean,
        var stockWording: String,
        var stock: Int,
        var minOrder: Int,
        var maxOrder: Int,
        var optionsId: ArrayList<Int>
) : Parcelable {

    constructor(parcel: Parcel? = null) : this(
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readByte() != 0.toByte(),
            parcel?.readByte() != 0.toByte(),
            parcel?.readString() ?: "",
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            arrayListOf<Int>().apply {
                parcel?.readList(this, Int::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)
        parcel.writeString(productName)
        parcel.writeInt(productPrice)
        parcel.writeString(productImageUrl)
        parcel.writeByte(if (isAvailable) 1 else 0)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeString(stockWording)
        parcel.writeInt(stock)
        parcel.writeInt(minOrder)
        parcel.writeInt(maxOrder)
        parcel.writeList(optionsId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductChild> {
        override fun createFromParcel(parcel: Parcel): ProductChild {
            return ProductChild(parcel)
        }

        override fun newArray(size: Int): Array<ProductChild?> {
            return arrayOfNulls(size)
        }
    }

}