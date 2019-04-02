package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class ShopInfoModel {
    @SerializedName("shop_name")
    @Expose
    private String shopName = "";
    @SerializedName("shop_avatar")
    @Expose
    private String shopAvatar = "";
    @SerializedName("shop_id")
    @Expose
    private String shopId = "";
    @SerializedName("shop_domain")
    @Expose
    private String shopDomain = "";
    @SerializedName("shop_is_official")
    @Expose
    private String shopIsOfficial = "";

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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getShopIsOfficial() {
        return shopIsOfficial;
    }

    public void setShopIsOfficial(String shopIsOfficial) {
        this.shopIsOfficial = shopIsOfficial;
    }
}
