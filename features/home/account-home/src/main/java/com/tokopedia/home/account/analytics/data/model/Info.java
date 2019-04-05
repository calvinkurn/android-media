
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("__typename")
    @Expose
    private String typename = "";
    @SerializedName("date_shop_created")
    @Expose
    private String dateShopCreated = "";
    @SerializedName("shop_id")
    @Expose
    private String shopId = "";
    @SerializedName("shop_location")
    @Expose
    private String shopLocation = "";
    @SerializedName("shop_name")
    @Expose
    private String shopName = "";
    @SerializedName("shop_score")
    @Expose
    private Integer shopScore = 0;
    @SerializedName("total_active_product")
    @Expose
    private Integer totalActiveProduct = 0;
    @SerializedName("shop_avatar")
    @Expose
    private String shopAvatar = "";
    @SerializedName("shop_cover")
    @Expose
    private String shopCover = "";
    @SerializedName("shop_domain")
    @Expose
    private String shopDomain = "";

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getDateShopCreated() {
        return dateShopCreated;
    }

    public void setDateShopCreated(String dateShopCreated) {
        this.dateShopCreated = dateShopCreated;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getShopScore() {
        return shopScore;
    }

    public void setShopScore(Integer shopScore) {
        this.shopScore = shopScore;
    }

    public Integer getTotalActiveProduct() {
        return totalActiveProduct;
    }

    public void setTotalActiveProduct(Integer totalActiveProduct) {
        this.totalActiveProduct = totalActiveProduct;
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

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

}
