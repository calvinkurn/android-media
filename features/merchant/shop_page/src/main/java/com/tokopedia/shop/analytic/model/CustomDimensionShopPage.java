package com.tokopedia.shop.analytic.model;

import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo;

/**
 * Created by hendry on 11/10/18.
 */
public class CustomDimensionShopPage {
    public String shopId;
    @TrackShopTypeDef
    public String shopType;

    public static CustomDimensionShopPage create(ShopInfo shopInfo){
        CustomDimensionShopPage customDimensionShopPage = new CustomDimensionShopPage();
        customDimensionShopPage.shopId = shopInfo.getShopCore().getShopID();
        customDimensionShopPage.shopType = shopInfo.getGoldOS().isOfficial() == 1 ? TrackShopTypeDef.OFFICIAL_STORE:
                shopInfo.getGoldOS().isGold() == 1 ? TrackShopTypeDef.GOLD_MERCHANT: TrackShopTypeDef.REGULAR_MERCHANT;
        return customDimensionShopPage;
    }

    public static CustomDimensionShopPage create(String shopId, boolean isOfficialStore, boolean isGoldMerchant){
        CustomDimensionShopPage customDimensionShopPage = new CustomDimensionShopPage();
        customDimensionShopPage.shopId = shopId;
        customDimensionShopPage.shopType = isOfficialStore ? TrackShopTypeDef.OFFICIAL_STORE:
                isGoldMerchant ? TrackShopTypeDef.GOLD_MERCHANT: TrackShopTypeDef.REGULAR_MERCHANT;
        return customDimensionShopPage;
    }

    public void updateCustomDimensionData(String shopId, boolean isOfficialStore, boolean isGoldMerchant){
        this.shopId = shopId;
        shopType = isOfficialStore ? TrackShopTypeDef.OFFICIAL_STORE:
                isGoldMerchant ? TrackShopTypeDef.GOLD_MERCHANT: TrackShopTypeDef.REGULAR_MERCHANT;
    }


}
