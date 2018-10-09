package com.tokopedia.posapp.shop.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 4/3/18.
 */

public class ShopResponse {
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_avatar")
    @Expose
    private String shopAvatar;
    @SerializedName("shop_cover")
    @Expose
    private String shopCover;
    @SerializedName("shop_description")
    @Expose
    private String shopDescription;
    @SerializedName("shop_domain")
    @Expose
    private String shopDomain;
    @SerializedName("shop_tagline")
    @Expose
    private String shopTagline;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getShopTagline() {
        return shopTagline;
    }

    public void setShopTagline(String shopTagline) {
        this.shopTagline = shopTagline;
    }
}
