package com.tokopedia.shop_widget.common.analytic.model

import com.tokopedia.shop.common.constant.TrackShopTypeDef
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

/**
 * Created by hendry on 11/10/18.
 */
open class CustomDimensionShopWidget {
    var shopId: String? = null

    @TrackShopTypeDef
    var shopType: String? = null
    fun updateCustomDimensionData(shopId: String?, isOfficialStore: Boolean, isGoldMerchant: Boolean) {
        this.shopId = shopId
        shopType = if (isOfficialStore) TrackShopTypeDef.OFFICIAL_STORE else if (isGoldMerchant) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
    }

    companion object {
        fun create(shopInfo: ShopInfo): CustomDimensionShopWidget {
            val customDimensionShopWidget = CustomDimensionShopWidget()
            customDimensionShopWidget.shopId = shopInfo.shopCore.shopID
            customDimensionShopWidget.shopType = if (shopInfo.goldOS.isOfficial == 1) TrackShopTypeDef.OFFICIAL_STORE else if (shopInfo.goldOS.isGold == 1) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            return customDimensionShopWidget
        }

        fun create(shopId: String?, isOfficialStore: Boolean, isGoldMerchant: Boolean): CustomDimensionShopWidget {
            val customDimensionShopWidget = CustomDimensionShopWidget()
            customDimensionShopWidget.shopId = shopId
            customDimensionShopWidget.shopType = if (isOfficialStore) TrackShopTypeDef.OFFICIAL_STORE else if (isGoldMerchant) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            return customDimensionShopWidget
        }
    }
}