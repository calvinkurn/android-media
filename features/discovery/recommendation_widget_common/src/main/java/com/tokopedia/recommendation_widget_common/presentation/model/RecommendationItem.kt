package com.tokopedia.recommendation_widget_common.presentation.model

import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.kotlin.model.ImpressHolder

data class RecommendationItem(val productId: Int = 0,
                              val name: String = "",
                              val categoryBreadcrumbs: String = "",
                              val url: String = "",
                              val appUrl: String = "",
                              val clickUrl: String = "",
                              val wishlistUrl: String = "",
                              val trackerImageUrl: String = "",
                              val imageUrl: String = "",
                              val price: String = "",
                              val priceInt: Int = 0,
                              val departmentId: Int = 0,
                              val rating: Int = 0,
                              val countReview: Int = 0,
                              val stock: Int = 0,
                              val recommendationType: String = "",
                              val isTopAds: Boolean = false,
                              var isWishlist: Boolean = false,
                              val slashedPrice: String = "",
                              val slashedPriceInt: Int = 0,
                              val discountPercentageInt: Int = 0,
                              val discountPercentage: String = "",
                              val position: Int = 0,
                              val shopId: Int = 0,
                              val shopType: String = "",
                              val shopName: String = "",
                              var cartId: Int = 0,
                              val quantity: Int = 0,
                              val header: String = "",
                              val pageName: String = "",
                              val minOrder: Int = 0,
                              val location: String = "",
                              val badgesUrl: List<String?> = listOf(),
                              val type: String = "",
                              val isFreeOngkirActive: Boolean = false,
                              val freeOngkirImageUrl: String = "",
                              val labelPromo: RecommendationLabel = RecommendationLabel(),
                              val labelOffers: RecommendationLabel = RecommendationLabel(),
                              val labelCredibility: RecommendationLabel = RecommendationLabel(),
                              val isGold: Boolean = false): ImpressHolder(){

    fun getPriceIntFromString() = CurrencyFormatHelper.convertRupiahToInt(price)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecommendationItem

        if (productId != other.productId) return false
        if (name != other.name) return false
        if (categoryBreadcrumbs != other.categoryBreadcrumbs) return false
        if (url != other.url) return false
        if (appUrl != other.appUrl) return false
        if (clickUrl != other.clickUrl) return false
        if (wishlistUrl != other.wishlistUrl) return false
        if (trackerImageUrl != other.trackerImageUrl) return false
        if (imageUrl != other.imageUrl) return false
        if (price != other.price) return false
        if (priceInt != other.priceInt) return false
        if (departmentId != other.departmentId) return false
        if (rating != other.rating) return false
        if (countReview != other.countReview) return false
        if (stock != other.stock) return false
        if (recommendationType != other.recommendationType) return false
        if (isTopAds != other.isTopAds) return false
        if (isWishlist != other.isWishlist) return false
        if (slashedPrice != other.slashedPrice) return false
        if (slashedPriceInt != other.slashedPriceInt) return false
        if (discountPercentageInt != other.discountPercentageInt) return false
        if (discountPercentage != other.discountPercentage) return false
        if (position != other.position) return false
        if (shopId != other.shopId) return false
        if (shopType != other.shopType) return false
        if (shopName != other.shopName) return false
        if (cartId != other.cartId) return false
        if (quantity != other.quantity) return false
        if (header != other.header) return false
        if (pageName != other.pageName) return false
        if (minOrder != other.minOrder) return false
        if (location != other.location) return false
        if (badgesUrl != other.badgesUrl) return false
        if (type != other.type) return false
        if (isFreeOngkirActive != other.isFreeOngkirActive) return false
        if (freeOngkirImageUrl != other.freeOngkirImageUrl) return false
        if (labelPromo != other.labelPromo) return false
        if (labelOffers != other.labelOffers) return false
        if (labelCredibility != other.labelCredibility) return false
        if (isGold != other.isGold) return false

        return true
    }

    override fun hashCode(): Int {
        var result = productId
        result = 31 * result + name.hashCode()
        result = 31 * result + categoryBreadcrumbs.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + appUrl.hashCode()
        result = 31 * result + clickUrl.hashCode()
        result = 31 * result + wishlistUrl.hashCode()
        result = 31 * result + trackerImageUrl.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + priceInt
        result = 31 * result + departmentId
        result = 31 * result + rating
        result = 31 * result + countReview
        result = 31 * result + stock
        result = 31 * result + recommendationType.hashCode()
        result = 31 * result + isTopAds.hashCode()
        result = 31 * result + isWishlist.hashCode()
        result = 31 * result + slashedPrice.hashCode()
        result = 31 * result + slashedPriceInt
        result = 31 * result + discountPercentageInt
        result = 31 * result + discountPercentage.hashCode()
        result = 31 * result + position
        result = 31 * result + shopId
        result = 31 * result + shopType.hashCode()
        result = 31 * result + shopName.hashCode()
        result = 31 * result + cartId
        result = 31 * result + quantity
        result = 31 * result + header.hashCode()
        result = 31 * result + pageName.hashCode()
        result = 31 * result + minOrder
        result = 31 * result + location.hashCode()
        result = 31 * result + badgesUrl.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + isFreeOngkirActive.hashCode()
        result = 31 * result + freeOngkirImageUrl.hashCode()
        result = 31 * result + labelPromo.hashCode()
        result = 31 * result + labelOffers.hashCode()
        result = 31 * result + labelCredibility.hashCode()
        result = 31 * result + isGold.hashCode()
        return result
    }

}

data class RecommendationLabel(var title: String = "", val type: String = "")