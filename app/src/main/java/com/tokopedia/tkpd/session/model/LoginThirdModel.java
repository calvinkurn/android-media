package com.tokopedia.tkpd.session.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 17/11/2015.
 */
@Parcel
public class LoginThirdModel {
    @SerializedName("is_login")
    boolean isLogin;

    @SerializedName("is_register_device")
    int isRegisterDevice;

    @SerializedName("shop_is_gold")
    int shopIsGold;//shop_is_gold

    @SerializedName("shop_id")
    String shopId;// shop_id

    @SerializedName("status")
    int status;//status
    public static final int GOOGLE_PLUS_LOGIN = 1;
    public static final int FACEBOOK_LOGIN = 2;
    public static final int ERROR_LOGIN = 3;

    @SerializedName("shop_name")
    String  shopName;//shop_name

    @SerializedName("full_name")
    String fullName;

    @SerializedName("user_id")
    int userID;// dapat berarti google id atau facebook id

    @SerializedName("shop_has_terms")
    int shopHasTerms;//shop_has_terms

    @SerializedName("msisdn_show_dialog")
    int msisdnShowDialog;// msisdn_show_dialog

    @SerializedName("shop_avatar")
    String shopAvatar;// shop_avatar

    @SerializedName("user_image")
    String userImage;// user_image

    @SerializedName("msisdn_is_verified")
    String msisdnIsVerified;

    ShopRepModel shopRepModel;

    UserRepModel userRepModel;

    public ShopRepModel getShopRepModel() {
        return shopRepModel;
    }

    public void setShopRepModel(ShopRepModel shopRepModel) {
        this.shopRepModel = shopRepModel;
    }

    public UserRepModel getUserRepModel() {
        return userRepModel;
    }

    public void setUserRepModel(UserRepModel userRepModel) {
        this.userRepModel = userRepModel;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public int getIsRegisterDevice() {
        return isRegisterDevice;
    }

    public void setIsRegisterDevice(int isRegisterDevice) {
        this.isRegisterDevice = isRegisterDevice;
    }

    public String getMsisdnIsVerified() {
        return msisdnIsVerified;
    }

    public boolean isMsisdnVerified() {
        return msisdnIsVerified.equals("1");
    }

    public void setMsisdnIsVerified(String msisdnIsVerified) {
        this.msisdnIsVerified = msisdnIsVerified;
    }

    public int getShopIsGold() {
        return shopIsGold;
    }

    public void setShopIsGold(int shopIsGold) {
        this.shopIsGold = shopIsGold;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public int getShopHasTerms() {
        return shopHasTerms;
    }

    public void setShopHasTerms(int shopHasTerms) {
        this.shopHasTerms = shopHasTerms;
    }
}
