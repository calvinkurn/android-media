package com.tokopedia.shop_widget.common.analytic.model

import com.tokopedia.shop.common.constant.TrackShopTypeDef
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo

class CustomDimensionShopWidgetAttribution: CustomDimensionShopWidgetProduct() {

    @JvmField
    var attribution: String? = null
    @JvmField
    var isFulfillmentExist: Boolean? = null
    @JvmField
    var isFreeOngkirActive: Boolean? = null

    companion object {
        fun create(shopInfo: ShopInfo, productId: String?, attribution: String?, shopRef: String?): CustomDimensionShopWidgetAttribution {
            val customDimensionShopWidget = CustomDimensionShopWidgetAttribution()
            customDimensionShopWidget.shopId = shopInfo.info.shopId
            customDimensionShopWidget.shopType = if (shopInfo.info.isShopOfficial) TrackShopTypeDef.OFFICIAL_STORE else if (shopInfo.info.isGoldMerchant) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            customDimensionShopWidget.productId = productId
            customDimensionShopWidget.attribution = attribution
            customDimensionShopWidget.shopRef = shopRef.orEmpty()
            return customDimensionShopWidget
        }

        fun create(shopId: String?, isOfficial: Boolean, isGold: Boolean,
                   productId: String?, attribution: String?, shopRef: String?): CustomDimensionShopWidgetAttribution {
            val customDimensionShopWidget = CustomDimensionShopWidgetAttribution()
            customDimensionShopWidget.shopId = shopId
            customDimensionShopWidget.shopType = if (isOfficial) TrackShopTypeDef.OFFICIAL_STORE else if (isGold) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            customDimensionShopWidget.productId = productId
            customDimensionShopWidget.attribution = attribution
            customDimensionShopWidget.shopRef = shopRef.orEmpty()
            return customDimensionShopWidget
        }

        fun create(shopId: String?, isOfficial: Boolean, isGold: Boolean,
                   productId: String?, attribution: String?, shopRef: String?, isFulfillmentExist: Boolean, isFreeOngkirActive: Boolean): CustomDimensionShopWidgetAttribution {
            val customDimensionShopWidget = CustomDimensionShopWidgetAttribution()
            customDimensionShopWidget.shopId = shopId
            customDimensionShopWidget.shopType = if (isOfficial) TrackShopTypeDef.OFFICIAL_STORE else if (isGold) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            customDimensionShopWidget.productId = productId
            customDimensionShopWidget.attribution = attribution
            customDimensionShopWidget.shopRef = shopRef.orEmpty()
            customDimensionShopWidget.isFulfillmentExist = isFulfillmentExist
            customDimensionShopWidget.isFreeOngkirActive = isFreeOngkirActive
            return customDimensionShopWidget
        }
    }
}