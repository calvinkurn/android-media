package com.tokopedia.sessioncommon.data.model;

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
    private String msisdnIsVerified = "";
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName = "";
    @SerializedName("full_name")
    @Expose
    private String fullName = "";
    @SerializedName("is_login")
    @Expose
    private String isLogin = "";
    @SerializedName("shop_has_terms")
    @Expose
    private int shopHasTerms;
    @SerializedName("shop_is_official")
    @Expose
    private int shopIsOfficial;
    @SerializedName("is_register_device")
    @Expose
    private int isRegisterDevice;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("msisdn_show_dialog")
    @Expose
    private int msisdnShowDialog;
    @SerializedName("shop_avatar")
    @Expose
    private String shopAvatar = "";
    @SerializedName("user_image")
    @Expose
    private String userImage = "";
    @SerializedName("security")
    @Expose
    private SecurityPojo securityPojo = new SecurityPojo();

    public int getShopIsGold() {
        return shopIsGold;
    }

    public void setShopIsGold(int shopIsGold) {
        this.shopIsGold = shopIsGold;
    }

    public String getMsisdnIsVerified() {
        return msisdnIsVerified;
    }

    public void setMsisdnIsVerified(String msisdnIsVerified) {
        this.msisdnIsVerified = msisdnIsVerified;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
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

    /**
     * @return "true" or "false"
     */
    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public int getShopHasTerms() {
        return shopHasTerms;
    }

    public void setShopHasTerms(int shopHasTerms) {
        this.shopHasTerms = shopHasTerms;
    }

    public int getShopIsOfficial() {
        return shopIsOfficial;
    }

    public void setShopIsOfficial(int shopIsOfficial) {
        this.shopIsOfficial = shopIsOfficial;
    }

    public int getIsRegisterDevice() {
        return isRegisterDevice;
    }

    public void setIsRegisterDevice(int isRegisterDevice) {
        this.isRegisterDevice = isRegisterDevice;
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

    public SecurityPojo getSecurityPojo() {
        return securityPojo;
    }
}
