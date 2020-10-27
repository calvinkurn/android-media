package com.tokopedia.discovery.common.model

import android.os.Parcel
import android.os.Parcelable

data class ProductCardOptionsModel(
        var hasSimilarSearch: Boolean = false,
        var hasWishlist: Boolean = false,
        var hasAddToCart: Boolean = false,
        var hasVisitShop: Boolean = false,
        var hasShareProduct: Boolean = false,
        var isWishlisted: Boolean = false,
        var keyword: String = "",
        var productId: String = "",
        var isTopAds: Boolean = false,
        var topAdsWishlistUrl: String = "",
        var isRecommendation: Boolean = false,
        var productPosition: Int = 0,
        var screenName: String = "",
        var seeSimilarProductEvent: String = "",
        var addToCartParams: AddToCartParams? = null,
        var shop: Shop = Shop(),
        var productName: String = "",
        var categoryName: String = "",
        var formattedPrice: String = "",
        var productImageUrl: String = "",
        var productUrl: String = ""
): Parcelable {

    var wishlistResult: WishlistResult = WishlistResult()

    var addToCartResult: AddToCartResult = AddToCartResult()

    fun canAddToCart(): Boolean {
        return hasAddToCart
                && productId.isNotEmpty()
                && productName.isNotEmpty()
                && shop.shopId.isNotEmpty()
                && formattedPrice.isNotEmpty()
                && addToCartParams?.quantity ?: 0 > 0
    }

    fun canVisitShop(): Boolean {
        return hasVisitShop
                && shop.shopId.isNotEmpty()
    }

    fun canShareProduct(): Boolean {
        return hasShareProduct
                && productId.isNotEmpty()
                && productName.isNotEmpty()
                && productImageUrl.isNotEmpty()
                && productUrl.isNotEmpty()
                && formattedPrice.isNotEmpty()
                && shopName.isNotEmpty()
                && shopUrl.isNotEmpty()
    }

    val shopId: String
        get() = shop.shopId

    val shopName: String
        get() = shop.shopName

    val shopUrl: String
        get() = shop.shopUrl

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readParcelable(AddToCartParams::class.java.classLoader) ?: AddToCartParams(),
            parcel.readParcelable(Shop::class.java.classLoader) ?: Shop(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (hasSimilarSearch) 1 else 0)
        parcel.writeByte(if (hasWishlist) 1 else 0)
        parcel.writeByte(if (hasAddToCart) 1 else 0)
        parcel.writeByte(if (hasVisitShop) 1 else 0)
        parcel.writeByte(if (hasShareProduct) 1 else 0)
        parcel.writeByte(if (isWishlisted) 1 else 0)
        parcel.writeString(keyword)
        parcel.writeString(productId)
        parcel.writeByte(if (isTopAds) 1 else 0)
        parcel.writeString(topAdsWishlistUrl)
        parcel.writeByte(if (isRecommendation) 1 else 0)
        parcel.writeInt(productPosition)
        parcel.writeString(screenName)
        parcel.writeString(seeSimilarProductEvent)
        parcel.writeParcelable(addToCartParams, flags)
        parcel.writeParcelable(shop, flags)
        parcel.writeString(productName)
        parcel.writeString(categoryName)
        parcel.writeString(formattedPrice)
        parcel.writeString(productImageUrl)
        parcel.writeString(productUrl)
        parcel.writeParcelable(wishlistResult, flags)
        parcel.writeParcelable(addToCartResult, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductCardOptionsModel> {
        override fun createFromParcel(parcel: Parcel): ProductCardOptionsModel {
            return ProductCardOptionsModel(parcel).also {
                it.wishlistResult = parcel.readParcelable(WishlistResult::class.java.classLoader) ?: WishlistResult()
                it.addToCartResult = parcel.readParcelable(AddToCartResult::class.java.classLoader) ?: AddToCartResult()
            }
        }

        override fun newArray(size: Int): Array<ProductCardOptionsModel?> {
            return arrayOfNulls(size)
        }
    }

    data class WishlistResult(
            var isUserLoggedIn: Boolean = false,
            var isSuccess: Boolean = false,
            var isAddWishlist: Boolean = false
    ): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeByte(if (isUserLoggedIn) 1 else 0)
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

    data class AddToCartParams(
            var quantity: Int = 0
    ): Parcelable {

        constructor(parcel: Parcel) : this(parcel.readInt())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(quantity)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<AddToCartParams> {
            override fun createFromParcel(parcel: Parcel): AddToCartParams {
                return AddToCartParams(parcel)
            }

            override fun newArray(size: Int): Array<AddToCartParams?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class AddToCartResult(
            var isUserLoggedIn: Boolean = false,
            var isSuccess: Boolean = false,
            var cartId: String = "",
            var errorMessage: String = ""
    ): Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readString() ?: "",
                parcel.readString() ?: "")

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeByte(if (isUserLoggedIn) 1 else 0)
            parcel.writeByte(if (isSuccess) 1 else 0)
            parcel.writeString(cartId)
            parcel.writeString(errorMessage)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<AddToCartResult> {
            override fun createFromParcel(parcel: Parcel): AddToCartResult {
                return AddToCartResult(parcel)
            }

            override fun newArray(size: Int): Array<AddToCartResult?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Shop(
            var shopId: String = "",
            var shopName: String = "",
            var shopUrl: String = ""
    ): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "")

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(shopId)
            parcel.writeString(shopName)
            parcel.writeString(shopUrl)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Shop> {
            override fun createFromParcel(parcel: Parcel): Shop {
                return Shop(parcel)
            }

            override fun newArray(size: Int): Array<Shop?> {
                return arrayOfNulls(size)
            }
        }
    }
}