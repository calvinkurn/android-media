package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Topad {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("ad_ref_key")
    @Expose
    private String ad_ref_key;

    @SerializedName("redirect")
    @Expose
    private String redirect;

    @SerializedName("sticker_id")
    @Expose
    private String sticker_id;

    @SerializedName("sticker_image")
    @Expose
    private String sticker_image;

    @SerializedName("product_click_url")
    @Expose
    private String product_click_url;

    @SerializedName("shop_click_url")
    @Expose
    private String shop_click_url;

    @SerializedName("product")
    @Expose
    private Product product;

    @SerializedName("shop")
    @Expose
    private Shop shop;

    @SerializedName("applinks")
    @Expose
    private String applinks;

    public String getId() {
        return id;
    }

    public String getAd_ref_key() {
        return ad_ref_key;
    }

    public String getRedirect() {
        return redirect;
    }

    public String getSticker_id() {
        return sticker_id;
    }

    public String getSticker_image() {
        return sticker_image;
    }

    public String getProduct_click_url() {
        return product_click_url;
    }

    public String getShop_click_url() {
        return shop_click_url;
    }

    public Product getProduct() {
        return product;
    }

    public Shop getShop() {
        return shop;
    }

    public String getApplinks() {
        return applinks;
    }
}
