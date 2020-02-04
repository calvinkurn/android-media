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

    var wishlistResult: WishlistResult? = null

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
        parcel.writeParcelable(wishlistResult, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductCardOptionsModel> {
        override fun createFromParcel(parcel: Parcel): ProductCardOptionsModel {
            return ProductCardOptionsModel(parcel).also {
                it.wishlistResult = parcel.readParcelable(WishlistResult::class.java.classLoader)
            }
        }

        override fun newArray(size: Int): Array<ProductCardOptionsModel?> {
            return arrayOfNulls(size)
        }
    }

    data class WishlistResult(
            var isSuccess: Boolean = false,
            var isAddWishlist: Boolean = false
    ): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeByte(if (isSuccess) 1 else 0)
            parcel.writeByte(if (isAddWishlist) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<WishlistResult> {
            override fun createFromParcel(parcel: Parcel): WishlistResult {
                return WishlistResult(parcel)
            }

            override fun newArray(size: Int): Array<WishlistResult?> {
                return arrayOfNulls(size)
            }
        }
    }
}