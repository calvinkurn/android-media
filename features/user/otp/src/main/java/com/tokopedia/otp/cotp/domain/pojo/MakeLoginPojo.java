package com.tokopedia.otp.cotp.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginPojo {

    @SerializedName("shop_is_gold")
    @Expose
    private int shopIsGold;
    @SerializedName("msisdn_is_verified")
    @Expose
    private String msisdnIsVerified;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("is_login")
    @Expose
    private String isLogin;
    @SerializedName("user_id")
    @Expose
    private int userId;

    public int getShopIsGold() {
        return shopIsGold;
    }

    public String getMsisdnIsVerified() {
        return msisdnIsVerified;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
