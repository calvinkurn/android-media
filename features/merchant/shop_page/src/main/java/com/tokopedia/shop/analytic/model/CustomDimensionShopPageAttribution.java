package com.tokopedia.shop.analytic.model;

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

/**
 * Created by hendry on 11/10/18.
 */
public class CustomDimensionShopPageAttribution extends CustomDimensionShopPageProduct {
    public String attribution;

    public static CustomDimensionShopPageAttribution create(ShopInfo shopInfo, String productId, String attribution){
        CustomDimensionShopPageAttribution customDimensionShopPage = new CustomDimensionShopPageAttribution();
        customDimensionShopPage.shopId = shopInfo.getInfo().getShopId();
        customDimensionShopPage.shopType = shopInfo.getInfo().isShopOfficial() ? TrackShopTypeDef.OFFICIAL_STORE:
                shopInfo.getInfo().isGoldMerchant() ? TrackShopTypeDef.GOLD_MERCHANT: TrackShopTypeDef.REGULAR_MERCHANT;
        customDimensionShopPage.productId = productId;
        customDimensionShopPage.attribution = attribution;
        return customDimensionShopPage;
    }

    public static CustomDimensionShopPageAttribution create(String shopId, boolean isOfficial, boolean isGold,
                                                            String productId, String attribution){
        CustomDimensionShopPageAttribution customDimensionShopPage = new CustomDimensionShopPageAttribution();
        customDimensionShopPage.shopId = shopId;
        customDimensionShopPage.shopType = isOfficial ? TrackShopTypeDef.OFFICIAL_STORE:
                isGold ? TrackShopTypeDef.GOLD_MERCHANT: TrackShopTypeDef.REGULAR_MERCHANT;
        customDimensionShopPage.productId = productId;
        customDimensionShopPage.attribution = attribution;
        return customDimensionShopPage;
    }
}
