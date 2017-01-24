package com.tokopedia.core.drawer2.viewmodel;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerProfile {
    private String userName;
    private String userAvatar;
    private String shopName;
    private String shopAvatar;
    private String shopCover;
    private String deposit;
    private String topPoints;
    private String topCash;
    private String topPointsUrl;

    public DrawerProfile() {
        this.userName = "";
        this.userAvatar = "";
        this.shopName = "";
        this.shopCover = "";
        this.deposit = "";
        this.topPoints = "";
        this.topCash = "";
        this.topPointsUrl = "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public String getShopCover() {
        return shopCover;
    }

    public void setShopCover(String shopCover) {
        this.shopCover = shopCover;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getTopPoints() {
        return topPoints;
    }

    public void setTopPoints(String topPoints) {
        this.topPoints = topPoints;
    }

    public String getTopCash() {
        return topCash;
    }

    public void setTopCash(String topCash) {
        this.topCash = topCash;
    }

    public void setTopPointsUrl(String topPointsUrl) {
        this.topPointsUrl = topPointsUrl;
    }

    public String getTopPointsUrl() {
        return topPointsUrl;
    }
}
