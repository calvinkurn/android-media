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
  private String relative_uri;

  @SerializedName("price_format")
  @Expose
  private String price_format;

  @SerializedName("count_talk_format")
  @Expose
  private String count_talk_format;

  @SerializedName("count_review_format")
  @Expose
  private String count_review_format;

  @SerializedName("category")
  @Expose
  private TopAdsCategoryType category;

  @SerializedName("product_preorder")
  @Expose
  private Boolean product_preorder;

  @SerializedName("product_wholesale")
  @Expose
  private Boolean product_wholesale;

  @SerializedName("free_return")
  @Expose
  private String free_return;

  @SerializedName("product_cashback")
  @Expose
  private Boolean product_cashback;

  @SerializedName("product_cashback_rate")
  @Expose
  private String product_cashback_rate;

  @SerializedName("product_rating")
  @Expose
  private Integer product_rating;

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

  public void setRelative_uri(String relative_uri) {
    this.relative_uri = relative_uri;
  }

  public void setPrice_format(String price_format) {
    this.price_format = price_format;
  }

  public void setCount_talk_format(String count_talk_format) {
    this.count_talk_format = count_talk_format;
  }

  public void setCount_review_format(String count_review_format) {
    this.count_review_format = count_review_format;
  }

  public void setCategory(TopAdsCategoryType category) {
    this.category = category;
  }

  public void setProduct_preorder(Boolean product_preorder) {
    this.product_preorder = product_preorder;
  }

  public void setProduct_wholesale(Boolean product_wholesale) {
    this.product_wholesale = product_wholesale;
  }

  public void setFree_return(String free_return) {
    this.free_return = free_return;
  }

  public void setProduct_cashback(Boolean product_cashback) {
    this.product_cashback = product_cashback;
  }

  public void setProduct_cashback_rate(String product_cashback_rate) {
    this.product_cashback_rate = product_cashback_rate;
  }

  public void setProduct_rating(Integer product_rating) {
    this.product_rating = product_rating;
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

  public String getRelative_uri() {
    return this.relative_uri;
  }

  public String getPrice_format() {
    return this.price_format;
  }

  public String getCount_talk_format() {
    return this.count_talk_format;
  }

  public String getCount_review_format() {
    return this.count_review_format;
  }

  public TopAdsCategoryType getCategory() {
    return this.category;
  }

  public Boolean getProduct_preorder() {
    return this.product_preorder;
  }

  public Boolean getProduct_wholesale() {
    return this.product_wholesale;
  }

  public String getFree_return() {
    return this.free_return;
  }

  public Boolean getProduct_cashback() {
    return this.product_cashback;
  }

  public String getProduct_cashback_rate() {
    return this.product_cashback_rate;
  }

  public Integer getProduct_rating() {
    return this.product_rating;
  }
}
