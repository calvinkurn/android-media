package com.tokopedia.productcard

import android.os.Parcelable
import com.tokopedia.productcard.utils.*
import kotlinx.android.parcel.Parcelize

data class ProductCardModel (
        val productImageUrl: String = "",
        @Deprecated("Cannot wishlist from product card anymore")
        var isWishlisted: Boolean = false,
        @Deprecated("Cannot wishlist from product card anymore")
        val isWishlistVisible: Boolean = false,
        @Deprecated("replace with labelGroupList")
        val labelPromo: Label = Label(),
        @Deprecated("No more shop image")
        val shopImageUrl: String = "",
        @Deprecated("No more shop name")
        val shopName: String = "",
        val productName: String = "",
        val discountPercentage: String = "",
        val slashedPrice: String = "",
        val priceRange: String = "",
        val formattedPrice: String = "",
        val shopBadgeList: List<ShopBadge> = listOf(),
        val shopLocation: String = "",
        val ratingCount: Int = 0,
        val reviewCount: Int = 0,
        @Deprecated("replace with labelGroupList")
        val labelCredibility: Label = Label(),
        @Deprecated("replace with labelGroupList")
        val labelOffers: Label = Label(),
        val freeOngkir: FreeOngkir = FreeOngkir(),
        val isTopAds: Boolean = false,
        val ratingString: String = "",
        val hasThreeDots: Boolean = false,
        val labelGroupList: List<LabelGroup> = listOf(),
        val hasDeleteProductButton: Boolean = false,
        val hasAddToCartButton: Boolean = false,
        val hasRemoveFromWishlistButton: Boolean = false,
        val pdpViewCount: String = "",
        val stockBarLabel: String = "",
        val stockBarPercentage: Int = 0,
        val isOutOfStock: Boolean = false,
        val addToCardText: String = "",
        val shopRating: String = "",
        val isShopRatingYellow: Boolean = false
) {
    @Deprecated("replace with labelGroupList")
    var isProductSoldOut: Boolean = false
    @Deprecated("replace with labelGroupList")
    var isProductPreOrder: Boolean = false
    @Deprecated("replace with labelGroupList")
    var isProductWholesale: Boolean = false

    @Deprecated("replace with LabelGroup")
    data class Label(
            val position: String = "",
            val title: String = "",
            val type: String = ""
    )

    data class FreeOngkir(
            val isActive: Boolean = false,
            val imageUrl: String = ""
    )

    data class ShopBadge(
            val isShown: Boolean = true,
            val imageUrl: String = ""
    )

    @Parcelize
    data class LabelGroup(
            val position: String = "",
            val title: String = "",
            val type: String = ""
    ):Parcelable

    fun getLabelProductStatus(): LabelGroup? {
        return findLabelGroup(LABEL_PRODUCT_STATUS)
    }

    private fun findLabelGroup(position: String): LabelGroup? {
        return labelGroupList.find { it.position == position }
    }

    fun getLabelPrice(): LabelGroup? {
        return findLabelGroup(LABEL_PRICE)
    }

    fun getLabelGimmick(): LabelGroup? {
        return findLabelGroup(LABEL_GIMMICK)
    }

    fun getLabelIntegrity(): LabelGroup? {
        return findLabelGroup(LABEL_INTEGRITY)
    }

    fun getLabelShipping(): LabelGroup? {
        return findLabelGroup(LABEL_SHIPPING)
    }

    fun willShowRatingAndReviewCount(): Boolean {
        return (ratingString.isNotEmpty() || ratingCount > 0) && reviewCount > 0
    }

    fun isShowDiscountOrSlashPrice() = discountPercentage.isNotEmpty() || slashedPrice.isNotEmpty()

    fun isShowFreeOngkirBadge() = freeOngkir.isActive && freeOngkir.imageUrl.isNotEmpty()

    fun isShowShopBadge() = shopBadgeList.find { it.isShown && it.imageUrl.isNotEmpty() } != null && shopLocation.isNotEmpty()

    fun isShowShopRating() = shopRating.isNotEmpty()
}