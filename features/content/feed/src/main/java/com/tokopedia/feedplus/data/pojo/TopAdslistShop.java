package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopAdslistShop {
  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("domain")
  @Expose
  private String domain;

  @SerializedName("tagline")
  @Expose
  private String tagline;

  @SerializedName("location")
  @Expose
  private String location;

  @SerializedName("city")
  @Expose
  private String city;

  @SerializedName("image_product")
  @Expose
  private List<TopAdslistShopProduct> imageProduct;

  @SerializedName("image_shop")
  @Expose
  private TopAdslistImageShop imageShop;

  @SerializedName("gold_shop")
  @Expose
  private Boolean goldShop;

  @SerializedName("lucky_shop")
  @Expose
  private String luckyShop;

  @SerializedName("shop_is_official")
  @Expose
  private Boolean shopIsOfficial;

  @SerializedName("owner_id")
  @Expose
  private String ownerId;

  @SerializedName("is_owner")
  @Expose
  private Boolean isOwner;

  @SerializedName("badges")
  @Expose
  private List<TopAdsBadge> badges;

  @SerializedName("uri")
  @Expose
  private String uri;

  @SerializedName("gold_shop_badge")
  @Expose
  private Boolean goldShopBadge;

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public void setTagline(String tagline) {
    this.tagline = tagline;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setImageProduct(List<TopAdslistShopProduct> imageProduct) {
    this.imageProduct = imageProduct;
  }

  public void setImageShop(TopAdslistImageShop imageShop) {
    this.imageShop = imageShop;
  }

  public void setGoldShop(Boolean goldShop) {
    this.goldShop = goldShop;
  }

  public void setLuckyShop(String luckyShop) {
    this.luckyShop = luckyShop;
  }

  public void setShopIsOfficial(Boolean shopIsOfficial) {
    this.shopIsOfficial = shopIsOfficial;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public void setIsOwner(Boolean isOwner) {
    this.isOwner = isOwner;
  }

  public void setBadges(List<TopAdsBadge> badges) {
    this.badges = badges;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public void setGoldShopBadge(Boolean goldShopBadge) {
    this.goldShopBadge = goldShopBadge;
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getDomain() {
    return this.domain;
  }

  public String getTagline() {
    return this.tagline;
  }

  public String getLocation() {
    return this.location;
  }

  public String getCity() {
    return this.city;
  }

  public List<TopAdslistShopProduct> getImageProduct() {
    return this.imageProduct;
  }

  public TopAdslistImageShop getImageShop() {
    return this.imageShop;
  }

  public Boolean getGoldShop() {
    return this.goldShop;
  }

  public String getLuckyShop() {
    return this.luckyShop;
  }

  public Boolean getShopIsOfficial() {
    return this.shopIsOfficial;
  }

  public String getOwnerId() {
    return this.ownerId;
  }

  public Boolean getIsOwner() {
    return this.isOwner;
  }

  public List<TopAdsBadge> getBadges() {
    return this.badges;
  }

  public String getUri() {
    return this.uri;
  }

  public Boolean getGoldShopBadge() {
    return this.goldShopBadge;
  }
}
