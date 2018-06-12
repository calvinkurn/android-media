package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedBanner {
  @SerializedName("img_url")
  @Expose
  private String img_url;

  @SerializedName("click_url")
  @Expose
  private String click_url;

  @SerializedName("click_applink")
  @Expose
  private String click_applink;

  @SerializedName("width_to_height_ratio")
  @Expose
  private Float width_to_height_ratio;

  public void setImg_url(String img_url) {
    this.img_url = img_url;
  }

  public void setClick_url(String click_url) {
    this.click_url = click_url;
  }

  public void setClick_applink(String click_applink) {
    this.click_applink = click_applink;
  }

  public void setWidth_to_height_ratio(Float width_to_height_ratio) {
    this.width_to_height_ratio = width_to_height_ratio;
  }

  public String getImg_url() {
    return this.img_url;
  }

  public String getClick_url() {
    return this.click_url;
  }

  public String getClick_applink() {
    return this.click_applink;
  }

  public Float getWidth_to_height_ratio() {
    return this.width_to_height_ratio;
  }
}
