
package com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductOwner implements Parcelable{

    @SerializedName("shop_id")
    @Expose
    int shopId;
    @SerializedName("user_label_id")
    @Expose
    int userLabelId;
    @SerializedName("user_url")
    @Expose
    String userUrl;
    @SerializedName("shop_img")
    @Expose
    String shopImg;
    @SerializedName("shop_url")
    @Expose
    String shopUrl;
    @SerializedName("shop_name")
    @Expose
    String shopName;
    @SerializedName("full_name")
    @Expose
    String fullName;
    @SerializedName("user_img")
    @Expose
    String userImg;
    @SerializedName("user_label")
    @Expose
    String userLabel;
    @SerializedName("user_id")
    @Expose
    int userId;
    @SerializedName("shop_reputation_badge")
    @Expose
    String shopReputationBadge;
    @SerializedName("shop_reputation_score")
    @Expose
    String shopReputationScore;

    protected ProductOwner(Parcel in) {
        shopId = in.readInt();
        userLabelId = in.readInt();
        userUrl = in.readString();
        shopImg = in.readString();
        shopUrl = in.readString();
        shopName = in.readString();
        fullName = in.readString();
        userImg = in.readString();
        userLabel = in.readString();
        userId = in.readInt();
        shopReputationBadge = in.readString();
        shopReputationScore = in.readString();
    }

    public static final Creator<ProductOwner> CREATOR = new Creator<ProductOwner>() {
        @Override
        public ProductOwner createFromParcel(Parcel in) {
            return new ProductOwner(in);
        }

        @Override
        public ProductOwner[] newArray(int size) {
            return new ProductOwner[size];
        }
    };

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getUserLabelId() {
        return userLabelId;
    }

    public void setUserLabelId(int userLabelId) {
        this.userLabelId = userLabelId;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
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

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getShopReputationBadge() {
        return shopReputationBadge;
    }

    public void setShopReputationBadge(String shopReputationBadge) {
        this.shopReputationBadge = shopReputationBadge;
    }

    public String getShopReputationScore() {
        return shopReputationScore;
    }

    public void setShopReputationScore(String shopReputationScore) {
        this.shopReputationScore = shopReputationScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shopId);
        dest.writeInt(userLabelId);
        dest.writeString(userUrl);
        dest.writeString(shopImg);
        dest.writeString(shopUrl);
        dest.writeString(shopName);
        dest.writeString(fullName);
        dest.writeString(userImg);
        dest.writeString(userLabel);
        dest.writeInt(userId);
        dest.writeString(shopReputationBadge);
        dest.writeString(shopReputationScore);
    }
}
