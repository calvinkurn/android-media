package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdslistProduct {
  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("image")
  @Expose
  private TopAdslistImage image;

  @SerializedName("uri")
  @Expose
  private String uri;

  @SerializedName("relative_uri")
  @Expose
  private String relativeUri;

  @SerializedName("price_format")
  @Expose
  private String priceFormat;

  @SerializedName("count_talk_format")
  @Expose
  private String countTalkFormat;

  @SerializedName("count_review_format")
  @Expose
  private String countReviewFormat;

  @SerializedName("category")
  @Expose
  private TopAdsCategoryType category;

  @SerializedName("product_preorder")
  @Expose
  private Boolean productPreorder;

  @SerializedName("product_wholesale")
  @Expose
  private Boolean productWholesale;

  @SerializedName("free_return")
  @Expose
  private String freeReturn;

  @SerializedName("product_cashback")
  @Expose
  private Boolean productCashback;

  @SerializedName("product_cashback_rate")
  @Expose
  private String productCashbackRate;

  @SerializedName("product_rating")
  @Expose
  private Integer productRating;

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setImage(TopAdslistImage image) {
    this.image = image;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public void setRelativeUri(String relativeUri) {
    this.relativeUri = relativeUri;
  }

  public void setPriceFormat(String priceFormat) {
    this.priceFormat = priceFormat;
  }

  public void setCountTalkFormat(String countTalkFormat) {
    this.countTalkFormat = countTalkFormat;
  }

  public void setCountReviewFormat(String countReviewFormat) {
    this.countReviewFormat = countReviewFormat;
  }

  public void setCategory(TopAdsCategoryType category) {
    this.category = category;
  }

  public void setProductPreorder(Boolean productPreorder) {
    this.productPreorder = productPreorder;
  }

  public void setProductWholesale(Boolean productWholesale) {
    this.productWholesale = productWholesale;
  }

  public void setFreeReturn(String freeReturn) {
    this.freeReturn = freeReturn;
  }

  public void setProductCashback(Boolean productCashback) {
    this.productCashback = productCashback;
  }

  public void setProductCashbackRate(String productCashbackRate) {
    this.productCashbackRate = productCashbackRate;
  }

  public void setProductRating(Integer productRating) {
    this.productRating = productRating;
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public TopAdslistImage getImage() {
    return this.image;
  }

  public String getUri() {
    return this.uri;
  }

  public String getRelativeUri() {
    return this.relativeUri;
  }

  public String getPriceFormat() {
    return this.priceFormat;
  }

  public String getCountTalkFormat() {
    return this.countTalkFormat;
  }

  public String getCountReviewFormat() {
    return this.countReviewFormat;
  }

  public TopAdsCategoryType getCategory() {
    return this.category;
  }

  public Boolean getProductPreorder() {
    return this.productPreorder;
  }

  public Boolean getProductWholesale() {
    return this.productWholesale;
  }

  public String getFreeReturn() {
    return this.freeReturn;
  }

  public Boolean getProductCashback() {
    return this.productCashback;
  }

  public String getProductCashbackRate() {
    return this.productCashbackRate;
  }

  public Integer getProductRating() {
    return this.productRating;
  }
}
