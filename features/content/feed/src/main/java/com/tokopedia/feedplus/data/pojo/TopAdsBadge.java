package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdsBadge {
  @SerializedName("title")
  @Expose
  private String title;

  @SerializedName("image_url")
  @Expose
  private String imageUrl;

  public void setTitle(String title) {
    this.title = title;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getTitle() {
    return this.title;
  }

  public String getImageUrl() {
    return this.imageUrl;
  }
}
