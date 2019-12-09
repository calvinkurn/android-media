package com.tokopedia.discovery.common.model

import android.os.Parcel
import android.os.Parcelable

data class ProductCardOptionsModel(
        var hasSimilarSearch: Boolean = false,
        var hasWishlist: Boolean = false,
        var isWishlisted: Boolean = false,
        var keyword: String = "",
        var productId: String = "",
        var isTopAds: Boolean = false,
        var screenName: String = "",
        var seeSimilarProductEvent: String = ""
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (hasSimilarSearch) 1 else 0)
        parcel.writeByte(if (hasWishlist) 1 else 0)
        parcel.writeByte(if (isWishlisted) 1 else 0)
        parcel.writeString(keyword)
        parcel.writeString(productId)
        parcel.writeByte(if (isTopAds) 1 else 0)
        parcel.writeString(screenName)
        parcel.writeString(seeSimilarProductEvent)
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