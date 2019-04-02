
package com.tokopedia.home.account.analytics.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfo {

    @SerializedName("shop_open_since")
    @Expose
    private String shopOpenSince = "";
    @SerializedName("shop_location")
    @Expose
    private String shopLocation = "";
    @SerializedName("shop_id")
    @Expose
    private String shopId = "";
    @SerializedName("shop_tagline")
    @Expose
    private String shopTagline = "";
    @SerializedName("shop_url")
    @Expose
    private String shopUrl = "";
    @SerializedName("shop_name")
    @Expose
    private String shopName = "";
    @SerializedName("shop_owner_id")
    @Expose
    private Integer shopOwnerId = 0;
    @SerializedName("shop_description")
    @Expose
    private String shopDescription = "";
    @SerializedName("shop_cover")
    @Expose
    private String shopCover = "";
    @SerializedName("shop_avatar")
    @Expose
    private String shopAvatar = "";
    @SerializedName("shop_domain")
    @Expose
    private String shopDomain = "";

    public String getShopOpenSince() {
        return shopOpenSince;
    }

    public void setShopOpenSince(String shopOpenSince) {
        this.shopOpenSince = shopOpenSince;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopTagline() {
        return shopTagline;
    }

    public void setShopTagline(String shopTagline) {
        this.shopTagline = shopTagline;
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

    public Integer getShopOwnerId() {
        return shopOwnerId;
    }

    public void setShopOwnerId(Integer shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getShopCover() {
        return shopCover;
    }

    public void setShopCover(String shopCover) {
        this.shopCover = shopCover;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    @Override
    public String toString() {
        return "ShopInfo{" +
                "shopOpenSince='" + shopOpenSince + '\'' +
                ", shopLocation='" + shopLocation + '\'' +
                ", shopId='" + shopId + '\'' +
                ", shopTagline='" + shopTagline + '\'' +
                ", shopUrl='" + shopUrl + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopOwnerId=" + shopOwnerId +
                ", shopDescription='" + shopDescription + '\'' +
                ", shopCover='" + shopCover + '\'' +
                ", shopAvatar='" + shopAvatar + '\'' +
                ", shopDomain='" + shopDomain + '\'' +
                '}';
    }
}
