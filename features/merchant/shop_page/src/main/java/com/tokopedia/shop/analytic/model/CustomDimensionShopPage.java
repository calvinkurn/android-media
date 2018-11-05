package com.tokopedia.shop.analytic.model;

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

/**
 * Created by hendry on 11/10/18.
 */
public class CustomDimensionShopPage {
    public String shopId;
    @TrackShopTypeDef
    public String shopType;

    public static CustomDimensionShopPage create(ShopInfo shopInfo){
        CustomDimensionShopPage customDimensionShopPage = new CustomDimensionShopPage();
        customDimensionShopPage.shopId = shopInfo.getInfo().getShopId();
        customDimensionShopPage.shopType = shopInfo.getInfo().isShopOfficial() ? TrackShopTypeDef.OFFICIAL_STORE:
                shopInfo.getInfo().isGoldMerchant() ? TrackShopTypeDef.GOLD_MERCHANT: TrackShopTypeDef.REGULAR_MERCHANT;
        return customDimensionShopPage;
    }
}
