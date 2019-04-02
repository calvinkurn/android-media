
package com.tokopedia.core.network.entity.intermediary.brands;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Brand {

    @SerializedName("shop_id")
    @Expose
    private Long shopId;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("logo_url")
    @Expose
    private String logoUrl;
    @SerializedName("is_new")
    @Expose
    private Long isNew;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
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

    public Long getIsNew() {
        return isNew;
    }

    public void setIsNew(Long isNew) {
        this.isNew = isNew;
    }

}
