package com.tokopedia.discovery.common.model

import android.os.Parcel
import android.os.Parcelable

class ProductCardOptionsModel(
        val hasSimilarSearch: Boolean = false,
        val hasWishlist: Boolean = false,
        val isWishlisted: Boolean = false,
        val keyword: String = "",
        val productId: String = "",
        val isTopAds: Boolean = false
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (hasSimilarSearch) 1 else 0)
        parcel.writeByte(if (hasWishlist) 1 else 0)
        parcel.writeByte(if (isWishlisted) 1 else 0)
        parcel.writeString(keyword)
        parcel.writeString(productId)
        parcel.writeByte(if (isTopAds) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductCardOptionsModel> {
        override fun createFromParcel(parcel: Parcel): ProductCardOptionsModel {
            return ProductCardOptionsModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductCardOptionsModel?> {
            return arrayOfNulls(size)
        }
    }
}