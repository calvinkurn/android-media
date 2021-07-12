package com.tokopedia.shop.analytic.model

import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

/**
 * Created by hendry on 11/10/18.
 */
open class CustomDimensionShopPage {
    var shopId: String? = null

    @TrackShopTypeDef
    var shopType: String? = null
    fun updateCustomDimensionData(shopId: String?, isOfficialStore: Boolean, isGoldMerchant: Boolean) {
        this.shopId = shopId
        shopType = if (isOfficialStore) TrackShopTypeDef.OFFICIAL_STORE else if (isGoldMerchant) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
    }

    companion object {
        fun create(shopInfo: ShopInfo): CustomDimensionShopPage {
            val customDimensionShopPage = CustomDimensionShopPage()
            customDimensionShopPage.shopId = shopInfo.shopCore.shopID
            customDimensionShopPage.shopType = if (shopInfo.goldOS.isOfficial == 1) TrackShopTypeDef.OFFICIAL_STORE else if (shopInfo.goldOS.isGold == 1) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            return customDimensionShopPage
        }

        fun create(shopId: String?, isOfficialStore: Boolean, isGoldMerchant: Boolean): CustomDimensionShopPage {
            val customDimensionShopPage = CustomDimensionShopPage()
            customDimensionShopPage.shopId = shopId
            customDimensionShopPage.shopType = if (isOfficialStore) TrackShopTypeDef.OFFICIAL_STORE else if (isGoldMerchant) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            return customDimensionShopPage
        }
    }
}