package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedSource {
  @SerializedName("origin")
  @Expose
  private Integer origin;

  @SerializedName("shop")
  @Expose
  private ShopDetail shop;

  @SerializedName("type")
  @Expose
  private Integer type;

  public void setOrigin(Integer origin) {
    this.origin = origin;
  }

  public void setShop(ShopDetail shop) {
    this.shop = shop;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getOrigin() {
    return this.origin;
  }

  public ShopDetail getShop() {
    return this.shop;
  }

  public Integer getType() {
    return this.type;
  }
}
