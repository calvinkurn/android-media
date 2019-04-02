package com.tokopedia.core.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by stevenfredian on 5/27/16.
 */

public class AccountsModel implements Parcelable {

    @SerializedName("is_login")
    @Expose
    private String isLogin;
    @SerializedName("shop_has_terms")
    @Expose
    private int shopHasTerms;
    @SerializedName("shop_reputation")
    @Expose
    private ShopReputation shopReputation;
    @SerializedName("shop_is_gold")
    @Expose
    private int shopIsGold;
    @SerializedName("is_register_device")
    @Expose
    private int isRegisterDevice;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("msisdn_is_verified")
    @Expose
    private String msisdnIsVerified;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_reputation")
    @Expose
    private UserReputation userReputation;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("msisdn_show_dialog")
    @Expose
    private int msisdnShowDialog;
    @SerializedName("shop_avatar")
    @Expose
    private String shopAvatar;
    @SerializedName("user_id")
    @Expose
    private int userId;

    /**
     *
     * @return
     *     The isLogin
     */
    public String getIsLogin() {
        return isLogin;
    }

    /**
     *
     * @param isLogin
     *     The is_login
     */
    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    /**
     *
     * @return
     *     The shopHasTerms
     */
    public int getShopHasTerms() {
        return shopHasTerms;
    }

    /**
     *
     * @param shopHasTerms
     *     The shop_has_terms
     */
    public void setShopHasTerms(int shopHasTerms) {
        this.shopHasTerms = shopHasTerms;
    }

    /**
     *
     * @return
     *     The shopReputation
     */
    public ShopReputation getShopReputation() {
        return shopReputation;
    }

    /**
     *
     * @param shopReputation
     *     The shop_reputation
     */
    public void setShopReputation(ShopReputation shopReputation) {
        this.shopReputation = shopReputation;
    }

    /**
     *
     * @return
     *     The shopIsGold
     */
    public int getShopIsGold() {
        return shopIsGold;
    }

    /**
     *
     * @param shopIsGold
     *     The shop_is_gold
     */
    public void setShopIsGold(int shopIsGold) {
        this.shopIsGold = shopIsGold;
    }

    /**
     *
     * @return
     *     The isRegisterDevice
     */
    public int getIsRegisterDevice() {
        return isRegisterDevice;
    }

    /**
     *
     * @param isRegisterDevice
     *     The is_register_device
     */
    public void setIsRegisterDevice(int isRegisterDevice) {
        this.isRegisterDevice = isRegisterDevice;
    }

    /**
     *
     * @return
     *     The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullName
     *     The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     *
     * @return
     *     The msisdnIsVerified
     */
    public String getMsisdnIsVerified() {
        return msisdnIsVerified;
    }

    public boolean getMsisdnIsVerifiedBoolean() {
        return msisdnIsVerified.equals("1");
    }

    /**
     *
     * @param msisdnIsVerified
     *     The msisdn_is_verified
     */
    public void setMsisdnIsVerified(String msisdnIsVerified) {
        this.msisdnIsVerified = msisdnIsVerified;
    }

    /**
     *
     * @return
     *     The shopId
     */
    public int getShopId() {
        return shopId;
    }

    /**
     *
     * @param shopId
     *     The shop_id
     */
    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    /**
     *
     * @return
     *     The userImage
     */
    public String getUserImage() {
        return userImage;
    }

    /**
     *
     * @param userImage
     *     The user_image
     */
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    /**
     *
     * @return
     *     The userReputation
     */
    public UserReputation getUserReputation() {
        return userReputation;
    }

    /**
     *
     * @param userReputation
     *     The user_reputation
     */
    public void setUserReputation(UserReputation userReputation) {
        this.userReputation = userReputation;
    }

    /**
     *
     * @return
     *     The shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     *
     * @param shopName
     *     The shop_name
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     *
     * @return
     *     The msisdnShowDialog
     */
    public int getMsisdnShowDialog() {
        return msisdnShowDialog;
    }

    /**
     *
     * @param msisdnShowDialog
     *     The msisdn_show_dialog
     */
    public void setMsisdnShowDialog(int msisdnShowDialog) {
        this.msisdnShowDialog = msisdnShowDialog;
    }

    /**
     *
     * @return
     *     The shopAvatar
     */
    public String getShopAvatar() {
        return shopAvatar;
    }

    /**
     *
     * @param shopAvatar
     *     The shop_avatar
     */
    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    /**
     *
     * @return
     *     The userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     *     The user_id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isLogin);
        dest.writeInt(this.shopHasTerms);
        dest.writeParcelable(this.shopReputation, flags);
        dest.writeInt(this.shopIsGold);
        dest.writeInt(this.isRegisterDevice);
        dest.writeString(this.fullName);
        dest.writeString(this.msisdnIsVerified);
        dest.writeInt(this.shopId);
        dest.writeString(this.userImage);
        dest.writeParcelable(this.userReputation, flags);
        dest.writeString(this.shopName);
        dest.writeInt(this.msisdnShowDialog);
        dest.writeString(this.shopAvatar);
        dest.writeInt(this.userId);
    }

    public AccountsModel() {
    }

    protected AccountsModel(Parcel in) {
        this.isLogin = in.readString();
        this.shopHasTerms = in.readInt();
        this.shopReputation = in.readParcelable(ShopReputation.class.getClassLoader());
        this.shopIsGold = in.readInt();
        this.isRegisterDevice = in.readInt();
        this.fullName = in.readString();
        this.msisdnIsVerified = in.readString();
        this.shopId = in.readInt();
        this.userImage = in.readString();
        this.userReputation = in.readParcelable(UserReputation.class.getClassLoader());
        this.shopName = in.readString();
        this.msisdnShowDialog = in.readInt();
        this.shopAvatar = in.readString();
        this.userId = in.readInt();
    }

    public static final Creator<AccountsModel> CREATOR = new Creator<AccountsModel>() {
        @Override
        public AccountsModel createFromParcel(Parcel source) {
            return new AccountsModel(source);
        }

        @Override
        public AccountsModel[] newArray(int size) {
            return new AccountsModel[size];
        }
    };
}
