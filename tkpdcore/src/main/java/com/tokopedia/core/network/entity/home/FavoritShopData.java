package com.tokopedia.core.network.entity.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by naveengoyal on 5/8/18.
 */

public class FavoritShopData {
    @SerializedName("data")
    @Expose
    FavoritShopResponseData data;

    public FavoritShopResponseData getData() {
        return data;
    }

    public void setData(FavoritShopResponseData data) {
        this.data = data;
    }
}
