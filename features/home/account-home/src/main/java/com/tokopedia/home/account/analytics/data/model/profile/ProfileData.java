
package com.tokopedia.home.account.analytics.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileData {

    @SerializedName("shop_stats")
    @Expose
    private ShopStats shopStats = new ShopStats();
    @SerializedName("shop_info")
    @Expose
    private ShopInfo shopInfo = new ShopInfo();
    @SerializedName("user_info")
    @Expose
    private UserInfo userInfo = new UserInfo();

    public ShopStats getShopStats() {
        return shopStats;
    }

    public void setShopStats(ShopStats shopStats) {
        this.shopStats = shopStats;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "data "+shopInfo.toString()+" "+userInfo.toString();
    }


}
