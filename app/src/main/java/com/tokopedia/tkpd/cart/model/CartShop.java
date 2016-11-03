
package com.tokopedia.tkpd.cart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartShop {

    @SerializedName("shop_pay_gateway")
    @Expose
    private List<Object> shopPayGateway = new ArrayList<Object>();
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_image")
    @Expose
    private String shopImage;
    @SerializedName("shop_status")
    @Expose
    private Integer shopStatus;
    @SerializedName("lucky_merchant")
    @Expose
    private Integer luckyMerchant;


    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public Integer getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    public Integer getLuckyMerchant() {
        return luckyMerchant;
    }

    public void setLuckyMerchant(Integer luckyMerchant) {
        this.luckyMerchant = luckyMerchant;
    }

    public List<Object> getShopPayGateway() {
        return shopPayGateway;
    }

    public void setShopPayGateway(List<Object> shopPayGateway) {
        this.shopPayGateway = shopPayGateway;
    }
}
