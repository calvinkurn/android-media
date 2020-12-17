package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 20/07/17.
 */

public class ProductOwnerDomain implements Parcelable {

    int shopId;
    int userLabelId;
    String userUrl;
    String shopImg;
    String shopUrl;
    String shopName;
    String fullName;
    String userImg;
    String userLabel;
    int userId;
    String shopReputationBadge;
    String shopReputationScore;

    public ProductOwnerDomain() {

    }

    protected ProductOwnerDomain(Parcel in) {
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

    public static final Creator<ProductOwnerDomain> CREATOR = new Creator<ProductOwnerDomain>() {
        @Override
        public ProductOwnerDomain createFromParcel(Parcel in) {
            return new ProductOwnerDomain(in);
        }

        @Override
        public ProductOwnerDomain[] newArray(int size) {
            return new ProductOwnerDomain[size];
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
