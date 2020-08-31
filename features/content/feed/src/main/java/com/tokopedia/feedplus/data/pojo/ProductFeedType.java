package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.TagsItem;

import java.util.List;

public class ProductFeedType {
  @SerializedName("id")
  @Expose
  private Integer id;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("price")
  @Expose
  private String price;

  @SerializedName("price_int")
  @Expose
  private Integer priceInt;

  @SerializedName("price_original")
  @Expose
  private String priceOriginal;

  @SerializedName("price_original_int")
  @Expose
  private Integer priceOriginalInt;

  @SerializedName("image")
  @Expose
  private String image;

  @SerializedName("image_single")
  @Expose
  private String imageSingle;

  @SerializedName("wholesale")
  @Expose
  private List<Wholesale> wholesale;

  @SerializedName("freereturns")
  @Expose
  private Boolean freereturns;

  @SerializedName("preorder")
  @Expose
  private Boolean preorder;

  @SerializedName("cashback")
  @Expose
  private String cashback;

  @SerializedName("url")
  @Expose
  private String url;

  @SerializedName("productLink")
  @Expose
  private String productLink;

  @SerializedName("wishlist")
  @Expose
  private Boolean wishlist;

  @SerializedName("rating")
  @Expose
  private Float rating;

  @SerializedName("countReview")
  @Expose
  private String countReview;

  @SerializedName("tags")
  @Expose
  private List<TagsItem> tags;


  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public void setPriceInt(Integer priceInt) {
    this.priceInt = priceInt;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setImageSingle(String imageSingle) {
    this.imageSingle = imageSingle;
  }

  public void setWholesale(List<Wholesale> wholesale) {
    this.wholesale = wholesale;
  }

  public void setFreereturns(Boolean freereturns) {
    this.freereturns = freereturns;
  }

  public void setPreorder(Boolean preorder) {
    this.preorder = preorder;
  }

  public void setCashback(String cashback) {
    this.cashback = cashback;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setProductLink(String productLink) {
    this.productLink = productLink;
  }

  public void setWishlist(Boolean wishlist) {
    this.wishlist = wishlist;
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }

  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getPrice() {
    return this.price;
  }

  public Integer getPriceInt() {
    return this.priceInt;
  }

  public String getImage() {
    return this.image;
  }

  public String getImageSingle() {
    return this.imageSingle;
  }

  public List<Wholesale> getWholesale() {
    return this.wholesale;
  }

  public Boolean getFreereturns() {
    return this.freereturns;
  }

  public Boolean getPreorder() {
    return this.preorder;
  }

  public String getCashback() {
    return this.cashback;
  }

  public String getUrl() {
    return this.url;
  }

  public String getProductLink() {
    return this.productLink;
  }

  public Boolean getWishlist() {
    return this.wishlist;
  }

  public Float getRating() {
    return this.rating;
  }

  public String getPriceOriginal() {
    return priceOriginal;
  }

  public void setPriceOriginal(String priceOriginal) {
    this.priceOriginal = priceOriginal;
  }

  public Integer getPriceOriginalInt() {
    return priceOriginalInt;
  }

  public void setPriceOriginalInt(Integer priceOriginalInt) {
    this.priceOriginalInt = priceOriginalInt;
  }

  public List<TagsItem> getTags() {
    return tags;
  }

  public void setTags(List<TagsItem> tags) {
    this.tags = tags;
  }

  public String getCountReview() {
    return countReview;
  }

  public void setCountReview(String countReview) {
    this.countReview = countReview;
  }
}
