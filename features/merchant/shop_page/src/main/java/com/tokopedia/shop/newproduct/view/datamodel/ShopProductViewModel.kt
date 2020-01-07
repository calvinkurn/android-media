package com.tokopedia.shop.newproduct.view.datamodel

import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct
import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductBadge
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductLabel

/**
 * Created by nathan on 2/6/18.
 */

class ShopProductViewModel : BaseShopProductViewModel {

    var id: String? = null
    var name: String? = null
    var displayedPrice: String? = null
    var originalPrice: String? = null
    var discountPercentage: String? = null
    var imageUrl: String? = null
    var imageUrl300: String? = null
    var imageUrl700: String? = null
    var totalReview: String? = null
    var rating: Double = 0.toDouble()
    var cashback: Double = 0.toDouble()
    var isWholesale: Boolean = false
    var isPo: Boolean = false
    var isFreeReturn: Boolean = false
    var isWishList: Boolean = false
    var productUrl: String? = null
    var isShowWishList: Boolean = false
    var isSoldOut: Boolean = false
    var isShowFreeOngkir: Boolean = false
    var freeOngkirPromoIcon: String? = null

    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    constructor() {}

    constructor(shopProduct: ShopProduct) {
        id = shopProduct.productId
        name = shopProduct.productName
        displayedPrice = shopProduct.productPrice
        imageUrl = shopProduct.productImage
        imageUrl300 = shopProduct.productImage300
        imageUrl700 = shopProduct.productImage700
        productUrl = shopProduct.productUrl
        rating = shopProduct.rating
        isPo = TextApiUtils.isValueTrue(shopProduct.productPreorder)
        totalReview = shopProduct.productReviewCount
        isWholesale = TextApiUtils.isValueTrue(shopProduct.productWholesale)
        if (shopProduct.badges != null && shopProduct.badges.size > 0) {
            for (badge in shopProduct.badges) {
                if (BADGE_FREE_RETURN.equals(badge.title, ignoreCase = true)) {
                    isFreeReturn = true
                    break
                }
            }
        }
        val shopProductLabelList = shopProduct.labels
        if (shopProductLabelList != null) {
            for (shopProductLabel in shopProductLabelList) {
                if (shopProductLabel.title.startsWith(LABEL_CASHBACK)) {
                    var cashbackText = shopProductLabel.title
                    cashbackText = cashbackText.replace(LABEL_CASHBACK, "")
                    cashbackText = cashbackText.replace(LABEL_PERCENTAGE, "")
                    val cashbackPercentage = java.lang.Double.parseDouble(cashbackText.trim { it <= ' ' })
                    cashback = cashbackPercentage
                    break
                }
            }
        }
        isSoldOut = shopProduct.isSoldOutStatus
    }

    constructor(gmFeaturedProduct: GMFeaturedProduct) {
        id = gmFeaturedProduct.productId
        name = gmFeaturedProduct.name
        displayedPrice = gmFeaturedProduct.price
        imageUrl = gmFeaturedProduct.imageUri
        productUrl = gmFeaturedProduct.uri

        totalReview = gmFeaturedProduct.totalReview
        rating = gmFeaturedProduct.rating
        if (gmFeaturedProduct.cashbackDetail != null) {
            cashback = gmFeaturedProduct.cashbackDetail.cashbackPercent
        }
        isWholesale = gmFeaturedProduct.isWholesale
        isPo = gmFeaturedProduct.isPreorder
        isFreeReturn = gmFeaturedProduct.isReturnable
    }

    companion object {

        private val BADGE_FREE_RETURN = "Free Return"
        private val LABEL_CASHBACK = "Cashback"
        private val LABEL_PERCENTAGE = "%"
    }

}
