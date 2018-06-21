package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdslistShopProduct {
  @SerializedName("product_id")
  @Expose
  private String productId;

  @SerializedName("product_name")
  @Expose
  private String productName;

  @SerializedName("image_url")
  @Expose
  private String imageUrl;

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getProductId() {
    return this.productId;
  }

  public String getProductName() {
    return this.productName;
  }

  public String getImageUrl() {
    return this.imageUrl;
  }
}
