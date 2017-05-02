package com.tokopedia.tkpd.home.favorite.domain.model;

import java.util.List;

/**
 * @author Kulomady on 1/19/17.
 */

public class TopAdsShop {
    private String message;
    private boolean isDataValid;
    private boolean isNetworkError;
    private List<TopAdsShopItem> topAdsShopItemList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }

    public List<TopAdsShopItem> getTopAdsShopItemList() {
        return topAdsShopItemList;
    }

    public void setTopAdsShopItemList(List<TopAdsShopItem> topAdsShopItemList) {
        this.topAdsShopItemList = topAdsShopItemList;
    }

    public boolean isNetworkError() {
        return isNetworkError;
    }

    public void setNetworkError(boolean networkError) {
        isNetworkError = networkError;
    }
}
