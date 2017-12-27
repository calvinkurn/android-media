package com.tokopedia.tkpd.beranda.domain.model.brands;

import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 11/28/17.
 */
public class BrandDataModel {

    @SerializedName("shop_id")
    private int shopId;
    @SerializedName("shop_url")
    private String shopUrl;
    @SerializedName("shop_name")
    private String shopName;
    @SerializedName("logo_url")
    private String logoUrl;
    @SerializedName("is_new")
    private int isNew;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
