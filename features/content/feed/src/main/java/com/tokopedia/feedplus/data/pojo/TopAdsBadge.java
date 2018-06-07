package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdsBadge {
  @SerializedName("title")
  @Expose
  private String title;

  @SerializedName("image_url")
  @Expose
  private String image_url;

  public void setTitle(String title) {
    this.title = title;
  }

  public void setImage_url(String image_url) {
    this.image_url = image_url;
  }

  public String getTitle() {
    return this.title;
  }

  public String getImage_url() {
    return this.image_url;
  }
}
