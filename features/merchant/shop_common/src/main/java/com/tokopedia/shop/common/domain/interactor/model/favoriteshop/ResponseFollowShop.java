
package com.tokopedia.shop.common.domain.interactor.model.favoriteshop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseFollowShop {

    @SerializedName("data")
    @Expose
    private DataFollowShop dataFollowShop;

    public DataFollowShop getDataFollowShop() {
        return dataFollowShop;
    }

    public void setDataFollowShop(DataFollowShop dataFollowShop) {
        this.dataFollowShop = dataFollowShop;
    }

}
