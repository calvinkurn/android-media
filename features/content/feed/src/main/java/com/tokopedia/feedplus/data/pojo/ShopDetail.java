package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopDetail {
  @SerializedName("id")
  @Expose
  private Integer id;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("avatar")
  @Expose
  private String avatar;

  @SerializedName("isOfficial")
  @Expose
  private Boolean isOfficial;

  @SerializedName("isGold")
  @Expose
  private Boolean isGold;

  @SerializedName("badge")
  @Expose
  private String badgeUrl;

  @SerializedName("url")
  @Expose
  private String url;

  @SerializedName("shopLink")
  @Expose
  private String shopLink;

  @SerializedName("shareLinkDescription")
  @Expose
  private String shareLinkDescription;

  @SerializedName("shareLinkURL")
  @Expose
  private String shareLinkURL;

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public void setIsOfficial(Boolean isOfficial) {
    this.isOfficial = isOfficial;
  }

  public void setIsGold(Boolean isGold) {
    this.isGold = isGold;
  }

  public void setBadgeUrl(String badgeUrl) {
    this.badgeUrl = badgeUrl;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setShopLink(String shopLink) {
    this.shopLink = shopLink;
  }

  public void setShareLinkDescription(String shareLinkDescription) {
    this.shareLinkDescription = shareLinkDescription;
  }

  public void setShareLinkURL(String shareLinkURL) {
    this.shareLinkURL = shareLinkURL;
  }

  public Integer getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getAvatar() {
    return this.avatar;
  }

  public Boolean getIsOfficial() {
    return this.isOfficial;
  }

  public Boolean getIsGold() {
    return this.isGold;
  }

  public String getBadgeUrl() {
    return badgeUrl;
  }

  public String getUrl() {
    return this.url;
  }

  public String getShopLink() {
    return this.shopLink;
  }

  public String getShareLinkDescription() {
    return this.shareLinkDescription;
  }

  public String getShareLinkURL() {
    return this.shareLinkURL;
  }
}
