package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdslistShopProduct {
  @SerializedName("product_id")
  @Expose
  private String product_id;

  @SerializedName("product_name")
  @Expose
  private String product_name;

  @SerializedName("image_url")
  @Expose
  private String image_url;

  public void setProduct_id(String product_id) {
    this.product_id = product_id;
  }

  public void setProduct_name(String product_name) {
    this.product_name = product_name;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }

  public String getProduct_id() {
    return this.product_id;
  }

  public String getProduct_name() {
    return this.product_name;
  }

  public String getImage_url() {
    return this.image_url;
  }
}
