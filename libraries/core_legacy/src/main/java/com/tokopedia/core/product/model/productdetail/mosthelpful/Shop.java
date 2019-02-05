
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shop {

    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("shop_logo")
    @Expose
    private String shopLogo;
    @SerializedName("shop_reputation")
    @Expose
    private ShopReputation shopReputation;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public ShopReputation getShopReputation() {
        return shopReputation;
    }

    public void setShopReputation(ShopReputation shopReputation) {
        this.shopReputation = shopReputation;
    }

}
