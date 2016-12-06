package com.tokopedia.core.session.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 05/11/2015.
 */
@Parcel
public class LoginInterruptModel {
    @SerializedName("shop_is_gold")
    int shopIsGold;
    @SerializedName("msisdn_is_verified")
    String msisdnIsVerified;
    @SerializedName("shop_id")
    int shopId;
    @SerializedName("shop_name")
    String shopName;
    @SerializedName("full_name")
    String fullName;
    @SerializedName("uuid")
    String uuid;
    @SerializedName("allow_login")
    int allowLogin;
    @SerializedName("is_login")
    boolean isLogin;
    @SerializedName("user_id")
    String userId;
    @SerializedName("msisdn_show_dialog")
    int msisdnShowDialog;
    @SerializedName("shop_avatar")
    String shopAvatar;
    @SerializedName("user_image")
    String userImage;

    public LoginInterruptModel(){}

    public int getShopIsGold() {
        return shopIsGold;
    }

    public void setShopIsGold(int shopIsGold) {
        this.shopIsGold = shopIsGold;
    }

    public boolean getMsisdnIsVerified() {
        return msisdnIsVerified.equals("1");
    }

    public void setMsisdnIsVerified(String msisdnIsVerified) {
        this.msisdnIsVerified = msisdnIsVerified;
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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(int allowLogin) {
        this.allowLogin = allowLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMsisdnShowDialog() {
        return msisdnShowDialog;
    }

    public void setMsisdnShowDialog(int msisdnShowDialog) {
        this.msisdnShowDialog = msisdnShowDialog;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
