package com.tokopedia.search.result.domain.model;

import java.util.ArrayList;
import java.util.List;

public class SearchShopModel {

    private List<ShopItem> shopItemList = new ArrayList<>();

    public List<ShopItem> getShopItemList() {
        return shopItemList;
    }

    public void setShopItemList(List<ShopItem> shopItemList) {
        this.shopItemList = shopItemList;
    }

    public static class ShopItem {

        private String shopId;
        private boolean isFavorited;

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public boolean isFavorited() {
            return isFavorited;
        }

        public void setFavorited(boolean favorited) {
            isFavorited = favorited;
        }
    }
}
