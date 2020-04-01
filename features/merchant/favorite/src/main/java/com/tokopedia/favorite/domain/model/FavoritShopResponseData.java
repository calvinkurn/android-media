package com.tokopedia.favorite.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavoritShopResponseData {
    @SerializedName("favorite_shop")
    @Expose
    FavShopItemData data;

    public FavShopItemData getData() {
        return data;
    }

    public void setData(FavShopItemData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FavoritShopResponseData{" +
                "data=" + data +
                '}';
    }
}
