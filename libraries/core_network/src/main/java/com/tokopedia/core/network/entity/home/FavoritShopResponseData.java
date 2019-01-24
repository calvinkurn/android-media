package com.tokopedia.core.network.entity.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by naveengoyal on 5/8/18.
 */

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
