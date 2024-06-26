package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

/**
 * Created by nathan on 2/6/18.
 */

class ShopProductUiModel : BaseShopProductViewModel, ImpressHolder {

    var id: String = ""
    var name: String = ""
    var displayedPrice: String = ""
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
    var productUrl: String = ""
    var isShowWishList: Boolean = false
    var isSoldOut: Boolean = false
    var isShowFreeOngkir: Boolean = false
    var freeOngkirPromoIcon: String? = null
    var etalaseId = ""
    var labelGroupList: List<LabelGroupUiModel> = listOf()
    var badge: List<ShopBadgeUiModel> = emptyList()
    var pdpViewCount: String = ""
    var stockLabel: String = ""
    var stockBarPercentage: Int = 0
    var isUpcoming: Boolean = false
    var etalaseType: Int? = null
    var hideGimmick: Boolean = false
    var isEnableDirectPurchase: Boolean = false
    var productInCart: Int = 0
    var isVariant: Boolean = false
    var isNewData: Boolean = false
    var stock: Long = 0
    var minimumOrder: Int = 0
    var maximumOrder: Int = 0
    var parentId: String = ""
    var averageRating: String = ""
    var isFulfillment: Boolean? = null
    var warehouseId: String? = null

    override fun type(typeFactory: ShopProductAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    constructor() {}

    constructor(shopProduct: ShopProduct) {
        id = shopProduct.productId.orEmpty()
        name = shopProduct.productName.orEmpty()
        displayedPrice = shopProduct.productPrice.orEmpty()
        imageUrl = shopProduct.productImage
        imageUrl300 = shopProduct.productImage300
        imageUrl700 = shopProduct.productImage700
        productUrl = shopProduct.productUrl.orEmpty()
        rating = shopProduct.rating
        isPo = TextApiUtils.isValueTrue(shopProduct.productPreorder)
        totalReview = shopProduct.productReviewCount
        isWholesale = TextApiUtils.isValueTrue(shopProduct.productWholesale)
        if (shopProduct.badges != null && shopProduct?.badges?.size.orZero() > 0) {
            for (badge in shopProduct.badges.orEmpty()) {
                if (BADGE_FREE_RETURN.equals(badge.title, ignoreCase = true)) {
                    isFreeReturn = true
                    break
                }
            }
        }
        val shopProductLabelList = shopProduct.labels
        if (shopProductLabelList != null) {
            for (shopProductLabel in shopProductLabelList) {
                if (shopProductLabel.title?.startsWith(LABEL_CASHBACK) == true) {
                    var cashbackText = shopProductLabel.title
                    cashbackText = cashbackText?.replace(LABEL_CASHBACK, "")
                    cashbackText = cashbackText?.replace(LABEL_PERCENTAGE, "")
                    val cashbackPercentage = java.lang.Double.parseDouble(cashbackText?.trim { it <= ' ' }.orEmpty())
                    cashback = cashbackPercentage
                    break
                }
            }
        }
        isSoldOut = shopProduct.isSoldOutStatus
    }

    constructor(gmFeaturedProduct: GMFeaturedProduct) {
        id = gmFeaturedProduct.productId.orEmpty()
        name = gmFeaturedProduct.name.orEmpty()
        displayedPrice = gmFeaturedProduct.price?.toString().orEmpty()
        imageUrl = gmFeaturedProduct.imageUri
        productUrl = gmFeaturedProduct.uri.orEmpty()

        totalReview = gmFeaturedProduct.totalReview
        rating = gmFeaturedProduct.rating
        gmFeaturedProduct.cashbackDetail?.let { cashbackDetail ->
            cashback = cashbackDetail.cashbackPercent
        }
        isWholesale = gmFeaturedProduct.isWholesale
        isPo = gmFeaturedProduct.isPreorder
        isFreeReturn = gmFeaturedProduct.isReturnable
    }

    companion object {

        private val BADGE_FREE_RETURN = "Free Return"
        private val LABEL_CASHBACK = "Cashback"
        private val LABEL_PERCENTAGE = "%"
        const val THRESHOLD_VIEW_COUNT = 1000
    }
}
