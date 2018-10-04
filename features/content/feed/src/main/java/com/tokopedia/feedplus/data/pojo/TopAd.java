package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAd {
  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("ad_ref_key")
  @Expose
  private String adRefKey;

  @SerializedName("redirect")
  @Expose
  private String redirect;

  @SerializedName("sticker_id")
  @Expose
  private String stickerId;

  @SerializedName("sticker_image")
  @Expose
  private String stickerImage;

  @SerializedName("product_click_url")
  @Expose
  private String productClickUrl;

  @SerializedName("shop_click_url")
  @Expose
  private String shopClickUrl;

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

  public void setAdRefKey(String adRefKey) {
    this.adRefKey = adRefKey;
  }

  public void setRedirect(String redirect) {
    this.redirect = redirect;
  }

  public void setStickerId(String stickerId) {
    this.stickerId = stickerId;
  }

  public void setStickerImage(String stickerImage) {
    this.stickerImage = stickerImage;
  }

  public void setProductClickUrl(String productClickUrl) {
    this.productClickUrl = productClickUrl;
  }

  public void setShopClickUrl(String shopClickUrl) {
    this.shopClickUrl = shopClickUrl;
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

  public String getAdRefKey() {
    return this.adRefKey;
  }

  public String getRedirect() {
    return this.redirect;
  }

  public String getStickerId() {
    return this.stickerId;
  }

  public String getStickerImage() {
    return this.stickerImage;
  }

  public String getProductClickUrl() {
    return this.productClickUrl;
  }

  public String getShopClickUrl() {
    return this.shopClickUrl;
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
