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
  private List<TopAdslistShopProduct> image_product;

  @SerializedName("image_shop")
  @Expose
  private TopAdslistImageShop image_shop;

  @SerializedName("gold_shop")
  @Expose
  private Boolean gold_shop;

  @SerializedName("lucky_shop")
  @Expose
  private String lucky_shop;

  @SerializedName("shop_is_official")
  @Expose
  private Boolean shop_is_official;

  @SerializedName("owner_id")
  @Expose
  private String owner_id;

  @SerializedName("is_owner")
  @Expose
  private Boolean is_owner;

  @SerializedName("badges")
  @Expose
  private List<TopAdsBadge> badges;

  @SerializedName("uri")
  @Expose
  private String uri;

  @SerializedName("gold_shop_badge")
  @Expose
  private Boolean gold_shop_badge;

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

  public void setImage_product(List<TopAdslistShopProduct> image_product) {
    this.image_product = image_product;
  }

  public void setImage_shop(TopAdslistImageShop image_shop) {
    this.image_shop = image_shop;
  }

  public void setGold_shop(Boolean gold_shop) {
    this.gold_shop = gold_shop;
  }

  public void setLucky_shop(String lucky_shop) {
    this.lucky_shop = lucky_shop;
  }

  public void setShop_is_official(Boolean shop_is_official) {
    this.shop_is_official = shop_is_official;
  }

  public void setOwner_id(String owner_id) {
    this.owner_id = owner_id;
  }

  public void setIs_owner(Boolean is_owner) {
    this.is_owner = is_owner;
  }

  public void setBadges(List<TopAdsBadge> badges) {
    this.badges = badges;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public void setGold_shop_badge(Boolean gold_shop_badge) {
    this.gold_shop_badge = gold_shop_badge;
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

  public List<TopAdslistShopProduct> getImage_product() {
    return this.image_product;
  }

  public TopAdslistImageShop getImage_shop() {
    return this.image_shop;
  }

  public Boolean getGold_shop() {
    return this.gold_shop;
  }

  public String getLucky_shop() {
    return this.lucky_shop;
  }

  public Boolean getShop_is_official() {
    return this.shop_is_official;
  }

  public String getOwner_id() {
    return this.owner_id;
  }

  public Boolean getIs_owner() {
    return this.is_owner;
  }

  public List<TopAdsBadge> getBadges() {
    return this.badges;
  }

  public String getUri() {
    return this.uri;
  }

  public Boolean getGold_shop_badge() {
    return this.gold_shop_badge;
  }
}
