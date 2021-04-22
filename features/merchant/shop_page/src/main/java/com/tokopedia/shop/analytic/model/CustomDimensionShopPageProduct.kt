package com.tokopedia.shop.analytic.model

import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.analytic.model.TrackShopTypeDef
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo

/**
 * Created by hendry on 11/10/18.
 */
open class CustomDimensionShopPageProduct : CustomDimensionShopPage() {
    @JvmField
    var productId: String? = null
    @JvmField
    var shopRef = ""

    companion object {
        fun create(shopInfo: ShopInfo, productId: String?, shopRef: String): CustomDimensionShopPageProduct {
            val customDimensionShopPage = CustomDimensionShopPageProduct()
            customDimensionShopPage.shopId = shopInfo.info.shopId
            customDimensionShopPage.shopType = if (shopInfo.info.isShopOfficial) TrackShopTypeDef.OFFICIAL_STORE else if (shopInfo.info.isGoldMerchant) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            customDimensionShopPage.productId = productId
            customDimensionShopPage.shopRef = shopRef
            return customDimensionShopPage
        }

        fun create(shopId: String?, isOfficial: Boolean, isGold: Boolean,
                   productId: String?, shopRef: String): CustomDimensionShopPageProduct {
            val customDimensionShopPage = CustomDimensionShopPageProduct()
            customDimensionShopPage.shopId = shopId
            customDimensionShopPage.shopType = if (isOfficial) TrackShopTypeDef.OFFICIAL_STORE else if (isGold) TrackShopTypeDef.GOLD_MERCHANT else TrackShopTypeDef.REGULAR_MERCHANT
            customDimensionShopPage.productId = productId
            customDimensionShopPage.shopRef = shopRef
            return customDimensionShopPage
        }
    }
}