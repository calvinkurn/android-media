package com.tokopedia.recommendation_widget_common.presentation.model

import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkProductInfo
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.extension.GRID_POS_BOTTOM_RIGHT
import com.tokopedia.recommendation_widget_common.extension.GRID_POS_LEFT
import com.tokopedia.recommendation_widget_common.extension.GRID_POS_TOP_RIGHT

data class RecommendationItem(
    val productId: Long = 0L,
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
    val warehouseId: Long = 0L,
    var cartId: String = "",
    var quantity: Int = 0,
    val header: String = "",
    val pageName: String = "",
    val minOrder: Int = 0,
    val maxOrder: Int = 0,
    val location: String = "",
    val badges: List<Badge> = listOf(),
    val type: String = "",
    val isFreeOngkirActive: Boolean = false,
    val freeOngkirImageUrl: String = "",
    val labelGroupList: List<RecommendationLabel> = listOf(),
    val isGold: Boolean = false,
    val isOfficial: Boolean = false,
    val specs: List<RecommendationSpecificationLabels> = listOf(),
    val addToCartType: AddToCartType = AddToCartType.None,
    val gridPosition: GridPosition = GridPosition.None,
    val appLog: RecommendationAppLog = RecommendationAppLog(),
    val countSold: Int = 0,
    // for tracker field
    val dimension61: String = "",
    val appLogImpressHolder: ImpressHolder = ImpressHolder(),
    // for tokonow
    val parentID: Long = 0L,
    var currentQuantity: Int = 0, // change this quantity before atc/update/delete, if failed then return this value to quantity
    val recommendationAdsLog: RecommendationAdsLog = RecommendationAdsLog()
) : ImpressHolder() {

    val productIdByteIo: String
        get() = if (parentID == 0L) productId.toString() else parentID.toString()

    enum class AddToCartType {
        DirectAtc,
        QuantityEditor,
        None
    }

    enum class GridPosition(val value: String) {
        None(""),
        Left(GRID_POS_LEFT),
        TopRight(GRID_POS_TOP_RIGHT),
        BottomRight(GRID_POS_BOTTOM_RIGHT)
    }

    data class Badge(
        val title: String,
        val imageUrl: String,
    )

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
        if (badges != other.badges) return false
        if (type != other.type) return false
        if (isFreeOngkirActive != other.isFreeOngkirActive) return false
        if (freeOngkirImageUrl != other.freeOngkirImageUrl) return false
        if (labelGroupList != other.labelGroupList) return false
        if (isGold != other.isGold) return false
        if (parentID != other.parentID) return false
        if (addToCartType != other.addToCartType) return false
        if (currentQuantity != other.currentQuantity) return false
        if (specs != other.specs) return false
        if (recommendationAdsLog != other.recommendationAdsLog) return false

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
        result = HASH_CODE * result + badges.hashCode()
        result = HASH_CODE * result + type.hashCode()
        result = HASH_CODE * result + isFreeOngkirActive.hashCode()
        result = HASH_CODE * result + freeOngkirImageUrl.hashCode()
        result = HASH_CODE * result + labelGroupList.hashCode()
        result = HASH_CODE * result + isGold.hashCode()
        result = HASH_CODE * result + isOfficial.hashCode()
        result = HASH_CODE * result + dimension61.hashCode()
        result = HASH_CODE * result + parentID.hashCode()
        result = HASH_CODE * result + addToCartType.hashCode()
        result = HASH_CODE * result + currentQuantity.hashCode()
        result = HASH_CODE * result + specs.hashCode()
        result = HASH_CODE * result + recommendationAdsLog.hashCode()
        return result
    }

    companion object {
        private const val HASH_CODE = 31
    }

    fun isProductHasParentID(): Boolean {
        return parentID != 0L
    }

    // default value 0
    fun setDefaultCurrentStock() {
        this.quantity = 0
        this.currentQuantity = 0
    }

    // func to update quantity from minicart
    fun updateItemCurrentStock(quantity: Int) {
        this.quantity = quantity
        this.currentQuantity = quantity
    }

    // call this when product card update values
    fun onCardQuantityChanged(updatedQuantity: Int) {
        currentQuantity = updatedQuantity
    }

    // call this when failed atc / update / delete
    fun onFailedUpdateCart() {
        currentQuantity = quantity
    }

    fun toAffiliateSdkProductInfo(): AffiliateSdkProductInfo =
        AffiliateSdkProductInfo(
            categoryID = "",
            isVariant = isProductHasParentID(),
            stockQty = currentQuantity
        )

    fun isUseQuantityEditor(): Boolean = addToCartType == AddToCartType.QuantityEditor

}

data class RecommendationSpecificationLabels(
    var specTitle: String = "",
    val specSummary: String = "",
    val recommendationSpecificationLabelsBullet: List<RecommendationSpecificationLabelsBullet> = listOf()
)

data class RecommendationSpecificationLabelsBullet(
    val specsSummary: String,
    val icon: String?
)
