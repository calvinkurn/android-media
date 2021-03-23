package com.tokopedia.recommendation_widget_common.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder

data class RecommendationItem(
        val productId: Int = 0,
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
        val ratingAverage: String = "",
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
        var cartId: String = "",
        val quantity: Int = 0,
        val header: String = "",
        val pageName: String = "",
        val minOrder: Int = 0,
        val location: String = "",
        val badgesUrl: List<String> = listOf(),
        val type: String = "",
        val isFreeOngkirActive: Boolean = false,
        val freeOngkirImageUrl: String = "",
        val labelGroupList: List<RecommendationLabel> = listOf(),
        val isGold: Boolean = false,
        val isOfficial:Boolean = false,
        // for tracker field
        val dimension61: String = "",
        val specs: List<RecommendationSpecificationLabels> = listOf()
): ImpressHolder(){

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
        if (ratingAverage != other.ratingAverage) return false
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
        if (labelGroupList != other.labelGroupList) return false
        if (isGold != other.isGold) return false

        return true
    }

    override fun hashCode(): Int {
        var result = productId.hashCode()
        result = HASH_CODE * result + name.hashCode()
        result = HASH_CODE * result + categoryBreadcrumbs.hashCode()
        result = HASH_CODE * result + url.hashCode()
        result = HASH_CODE * result + appUrl.hashCode()
        result = HASH_CODE * result + clickUrl.hashCode()
        result = HASH_CODE * result + wishlistUrl.hashCode()
        result = HASH_CODE * result + trackerImageUrl.hashCode()
        result = HASH_CODE * result + imageUrl.hashCode()
        result = HASH_CODE * result + price.hashCode()
        result = HASH_CODE * result + priceInt
        result = HASH_CODE * result + departmentId
        result = HASH_CODE * result + rating
        result = HASH_CODE * result + ratingAverage.hashCode()
        result = HASH_CODE * result + countReview
        result = HASH_CODE * result + stock
        result = HASH_CODE * result + recommendationType.hashCode()
        result = HASH_CODE * result + isTopAds.hashCode()
        result = HASH_CODE * result + isWishlist.hashCode()
        result = HASH_CODE * result + slashedPrice.hashCode()
        result = HASH_CODE * result + slashedPriceInt
        result = HASH_CODE * result + discountPercentageInt
        result = HASH_CODE * result + discountPercentage.hashCode()
        result = HASH_CODE * result + position
        result = HASH_CODE * result + shopId
        result = HASH_CODE * result + shopType.hashCode()
        result = HASH_CODE * result + shopName.hashCode()
        result = HASH_CODE * result + cartId.hashCode()
        result = HASH_CODE * result + quantity
        result = HASH_CODE * result + header.hashCode()
        result = HASH_CODE * result + pageName.hashCode()
        result = HASH_CODE * result + minOrder
        result = HASH_CODE * result + location.hashCode()
        result = HASH_CODE * result + badgesUrl.hashCode()
        result = HASH_CODE * result + type.hashCode()
        result = HASH_CODE * result + isFreeOngkirActive.hashCode()
        result = HASH_CODE * result + freeOngkirImageUrl.hashCode()
        result = HASH_CODE * result + labelGroupList.hashCode()
        result = HASH_CODE * result + isGold.hashCode()
        result = HASH_CODE * result + isOfficial.hashCode()
        result = HASH_CODE * result + dimension61.hashCode()
        return result
    }

    companion object{
        private const val HASH_CODE = 31
    }

}

data class RecommendationSpecificationLabels(var specTitle: String = "", val specSummary: String = "")