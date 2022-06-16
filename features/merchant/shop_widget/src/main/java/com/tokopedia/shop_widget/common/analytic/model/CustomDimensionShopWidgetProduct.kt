package com.tokopedia.shop_widget.common.analytic.model

import com.tokopedia.shop.common.constant.TrackShopTypeDef
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo

open class CustomDimensionShopWidgetProduct: CustomDimensionShopWidget() {

    @JvmField
    var productId: String? = null
    @JvmField
    var shopRef = ""

    companion object {
        fun create(shopInfo: ShopInfo, productId: String?, shopRef: String): CustomDimensionShopWidgetProduct {
            val customDimensionShopWidget = CustomDimensionShopWidgetProduct()
            customDimensionShopWidget.shopId = shopInfo.info.shopId
            customDimensionShopWidget.shopType = if (shopInfo.info.isShopOfficial) TrackShopTypeDef.OFFICIAL_STORE else if (shopInfo.info.isGoldMerchant) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            customDimensionShopWidget.productId = productId
            customDimensionShopWidget.shopRef = shopRef
            return customDimensionShopWidget
        }

        fun create(shopId: String?, isOfficial: Boolean, isGold: Boolean,
                   productId: String?, shopRef: String): CustomDimensionShopWidgetProduct {
            val customDimensionShopWidget = CustomDimensionShopWidgetProduct()
            customDimensionShopWidget.shopId = shopId
            customDimensionShopWidget.shopType = if (isOfficial) TrackShopTypeDef.OFFICIAL_STORE else if (isGold) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            customDimensionShopWidget.productId = productId
            customDimensionShopWidget.shopRef = shopRef
            return customDimensionShopWidget
        }
    }
}