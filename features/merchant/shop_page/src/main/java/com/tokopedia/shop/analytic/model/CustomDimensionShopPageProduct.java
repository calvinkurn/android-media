package com.tokopedia.shop.analytic.model;

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

/**
 * Created by hendry on 11/10/18.
 */
public class CustomDimensionShopPageProduct extends CustomDimensionShopPage {
    public String productId;

    public static CustomDimensionShopPageProduct create(ShopInfo shopInfo, String productId){
        CustomDimensionShopPageProduct customDimensionShopPage = new CustomDimensionShopPageProduct();
        customDimensionShopPage.shopId = shopInfo.getInfo().getShopId();
        customDimensionShopPage.shopType = shopInfo.getInfo().isShopOfficial() ? TrackShopTypeDef.OFFICIAL_STORE:
                shopInfo.getInfo().isGoldMerchant() ? TrackShopTypeDef.GOLD_MERCHANT: TrackShopTypeDef.REGULAR_MERCHANT;
        customDimensionShopPage.productId = productId;
        return customDimensionShopPage;
    }

    public static CustomDimensionShopPageProduct create(String shopId, boolean isOfficial, boolean isGold,
                                                        String productId){
        CustomDimensionShopPageProduct customDimensionShopPage = new CustomDimensionShopPageProduct();
        customDimensionShopPage.shopId = shopId;
        customDimensionShopPage.shopType = isOfficial ? TrackShopTypeDef.OFFICIAL_STORE:
                isGold ? TrackShopTypeDef.GOLD_MERCHANT: TrackShopTypeDef.REGULAR_MERCHANT;
        customDimensionShopPage.productId = productId;
        return customDimensionShopPage;
    }
}
