/*
 * Created By Kulomady on 11/26/16 1:32 AM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/26/16 1:32 AM
 */

package com.tokopedia.core.network.entity.discovery;

import com.tokopedia.core.var.RecyclerViewItem;

import org.parceler.Parcel;

/**
 * @author kulomady on 11/26/16.
 */

@Parcel
public class ShopModel extends RecyclerViewItem {
    public static final int SHOP_MODEL_TYPE = 1_912_123;
    String shopImage;
    String shopName;
    String totalTransaction;
    String numberOfFavorite;
    String shopId;
    String isGold;
    String luckyImage;
    boolean isOfficial;

    public ShopModel() {
        setType(SHOP_MODEL_TYPE);
    }

    public ShopModel(BrowseShopModel.Shops shop) {
        this();
        shopImage = shop.shopImage;
        shopName = shop.shopName;
        totalTransaction = shop.shopTotalTransaction;
        numberOfFavorite = shop.shopTotalFavorite;
        shopId = shop.shopId;
        isGold = shop.shopGoldShop;
        luckyImage = shop.shopLucky;
        isOfficial = shop.isOfficial;
    }

    public String getShopImage() {
        return shopImage;
    }

    public String getShopName() {
        return shopName;
    }

    public String getTotalTransaction() {
        return totalTransaction;
    }

    public String getNumberOfFavorite() {
        return numberOfFavorite;
    }

    public String getShopId() {
        return shopId;
    }

    public String getIsGold() {
        return isGold;
    }

    public String getLuckyImage() {
        return luckyImage;
    }

    public boolean isOfficial() {
        return isOfficial;
    }
}
