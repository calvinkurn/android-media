package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAd {
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
  private TopAdslistProduct product;

  @SerializedName("shop")
  @Expose
  private TopAdslistShop shop;

  @SerializedName("applinks")
  @Expose
  private String applinks;

  public void setId(String id) {
    this.id = id;
  }

  public void setAd_ref_key(String ad_ref_key) {
    this.ad_ref_key = ad_ref_key;
  }

  public void setRedirect(String redirect) {
    this.redirect = redirect;
  }

  public void setSticker_id(String sticker_id) {
    this.sticker_id = sticker_id;
  }

  public void setSticker_image(String sticker_image) {
    this.sticker_image = sticker_image;
  }

  public void setProduct_click_url(String product_click_url) {
    this.product_click_url = product_click_url;
  }

  public void setShop_click_url(String shop_click_url) {
    this.shop_click_url = shop_click_url;
  }

  public void setProduct(TopAdslistProduct product) {
    this.product = product;
  }

  public void setShop(TopAdslistShop shop) {
    this.shop = shop;
  }

  public void setApplinks(String applinks) {
    this.applinks = applinks;
  }

  public String getId() {
    return this.id;
  }

  public String getAd_ref_key() {
    return this.ad_ref_key;
  }

  public String getRedirect() {
    return this.redirect;
  }

  public String getSticker_id() {
    return this.sticker_id;
  }

  public String getSticker_image() {
    return this.sticker_image;
  }

  public String getProduct_click_url() {
    return this.product_click_url;
  }

  public String getShop_click_url() {
    return this.shop_click_url;
  }

  public TopAdslistProduct getProduct() {
    return this.product;
  }

  public TopAdslistShop getShop() {
    return this.shop;
  }

  public String getApplinks() {
    return this.applinks;
  }
}
