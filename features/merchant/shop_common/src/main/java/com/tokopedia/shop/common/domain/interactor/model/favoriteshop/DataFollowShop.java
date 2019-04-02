
package com.tokopedia.shop.common.domain.interactor.model.favoriteshop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataFollowShop {

    @SerializedName("followShop")
    @Expose
    private FollowShop followShop;

    public FollowShop getFollowShop() {
        return followShop;
    }

    public void setFollowShop(FollowShop followShop) {
        this.followShop = followShop;
    }

}
