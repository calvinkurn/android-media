package com.tokopedia.shop.settings.analytics.model

import com.tokopedia.shop.common.constant.TrackShopTypeDef
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

open class CustomDimensionShopPageSetting {
    var shopId: String? = null

    @TrackShopTypeDef
    var shopType: String? = null
    fun updateCustomDimensionData(shopId: String?, isOfficialStore: Boolean, isGoldMerchant: Boolean) {
        this.shopId = shopId
        shopType = if (isOfficialStore) TrackShopTypeDef.OFFICIAL_STORE else if (isGoldMerchant) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
    }

    companion object {
        fun create(shopInfo: ShopInfo): CustomDimensionShopPageSetting {
            val customDimensionShopPage = CustomDimensionShopPageSetting()
            customDimensionShopPage.shopId = shopInfo.shopCore.shopID
            customDimensionShopPage.shopType = if (shopInfo.goldOS.isOfficial == 1) TrackShopTypeDef.OFFICIAL_STORE else if (shopInfo.goldOS.isGold == 1) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            return customDimensionShopPage
        }

        fun create(shopId: String?, isOfficialStore: Boolean, isGoldMerchant: Boolean): CustomDimensionShopPageSetting {
            val customDimensionShopPage = CustomDimensionShopPageSetting()
            customDimensionShopPage.shopId = shopId
            customDimensionShopPage.shopType = if (isOfficialStore) TrackShopTypeDef.OFFICIAL_STORE else if (isGoldMerchant) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            return customDimensionShopPage
        }
    }
}