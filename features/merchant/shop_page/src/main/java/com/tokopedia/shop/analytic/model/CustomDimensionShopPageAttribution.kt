package com.tokopedia.shop.analytic.model

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo

/**
 * Created by hendry on 11/10/18.
 */
class CustomDimensionShopPageAttribution : CustomDimensionShopPageProduct() {
    @JvmField
    var attribution: String? = null
    @JvmField
    var isFulfillmentExist: Boolean? = null
    @JvmField
    var isFreeOngkirActive: Boolean? = null

    companion object {
        fun create(shopInfo: ShopInfo, productId: String?, attribution: String?, shopRef: String?): CustomDimensionShopPageAttribution {
            val customDimensionShopPage = CustomDimensionShopPageAttribution()
            customDimensionShopPage.shopId = shopInfo.info.shopId
            customDimensionShopPage.shopType = if (shopInfo.info.isShopOfficial) TrackShopTypeDef.OFFICIAL_STORE else if (shopInfo.info.isGoldMerchant) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            customDimensionShopPage.productId = productId
            customDimensionShopPage.attribution = attribution
            customDimensionShopPage.shopRef = shopRef.orEmpty()
            return customDimensionShopPage
        }

        fun create(shopId: String?, isOfficial: Boolean, isGold: Boolean,
                   productId: String?, attribution: String?, shopRef: String?): CustomDimensionShopPageAttribution {
            val customDimensionShopPage = CustomDimensionShopPageAttribution()
            customDimensionShopPage.shopId = shopId
            customDimensionShopPage.shopType = if (isOfficial) TrackShopTypeDef.OFFICIAL_STORE else if (isGold) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            customDimensionShopPage.productId = productId
            customDimensionShopPage.attribution = attribution
            customDimensionShopPage.shopRef = shopRef.orEmpty()
            return customDimensionShopPage
        }

        fun create(shopId: String?, isOfficial: Boolean, isGold: Boolean,
                   productId: String?, attribution: String?, shopRef: String?, isFulfillmentExist: Boolean, isFreeOngkirActive: Boolean): CustomDimensionShopPageAttribution {
            val customDimensionShopPage = CustomDimensionShopPageAttribution()
            customDimensionShopPage.shopId = shopId
            customDimensionShopPage.shopType = if (isOfficial) TrackShopTypeDef.OFFICIAL_STORE else if (isGold) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            customDimensionShopPage.productId = productId
            customDimensionShopPage.attribution = attribution
            customDimensionShopPage.shopRef = shopRef.orEmpty()
            customDimensionShopPage.isFulfillmentExist = isFulfillmentExist
            customDimensionShopPage.isFreeOngkirActive = isFreeOngkirActive
            return customDimensionShopPage
        }
    }
}