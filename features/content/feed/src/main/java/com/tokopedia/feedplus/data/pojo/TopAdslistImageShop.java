package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdslistImageShop {
  @SerializedName("cover_ecs")
  @Expose
  private String cover_ecs;

  @SerializedName("s_ecs")
  @Expose
  private String s_ecs;

  @SerializedName("xs_ecs")
  @Expose
  private String xs_ecs;

  @SerializedName("s_url")
  @Expose
  private String s_url;

  @SerializedName("xs_url")
  @Expose
  private String xs_url;

  public void setCover_ecs(String cover_ecs) {
    this.cover_ecs = cover_ecs;
  }

  public void setS_ecs(String s_ecs) {
    this.s_ecs = s_ecs;
  }

  public void setXs_ecs(String xs_ecs) {
    this.xs_ecs = xs_ecs;
  }

  public void setS_url(String s_url) {
    this.s_url = s_url;
  }

  public void setXs_url(String xs_url) {
    this.xs_url = xs_url;
  }

  public String getCover_ecs() {
    return this.cover_ecs;
  }

  public String getS_ecs() {
    return this.s_ecs;
  }

  public String getXs_ecs() {
    return this.xs_ecs;
  }

  public String getS_url() {
    return this.s_url;
  }

  public String getXs_url() {
    return this.xs_url;
  }
}
