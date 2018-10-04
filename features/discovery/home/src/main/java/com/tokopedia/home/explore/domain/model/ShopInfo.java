package com.tokopedia.home.explore.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by errysuprayogi on 2/8/18.
 */
public class ShopInfo {

    @SerializedName("data")
    private ShopData data;

    public ShopData getData() {
        return data;
    }

    public void setData(ShopData data) {
        this.data = data;
    }

}
