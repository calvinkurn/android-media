package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedBanner {
  @SerializedName("img_url")
  @Expose
  private String imgUrl;

  @SerializedName("click_url")
  @Expose
  private String clickUrl;

  @SerializedName("click_applink")
  @Expose
  private String clickApplink;

  @SerializedName("width_to_height_ratio")
  @Expose
  private Float widthToHeightRatio;

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public void setClickUrl(String clickUrl) {
    this.clickUrl = clickUrl;
  }

  public void setClickApplink(String clickApplink) {
    this.clickApplink = clickApplink;
  }

  public void setWidthToHeightRatio(Float widthToHeightRatio) {
    this.widthToHeightRatio = widthToHeightRatio;
  }

  public String getImgUrl() {
    return this.imgUrl;
  }

  public String getClickUrl() {
    return this.clickUrl;
  }

  public String getClickApplink() {
    return this.clickApplink;
  }

  public Float getWidthToHeightRatio() {
    return this.widthToHeightRatio;
  }
}
