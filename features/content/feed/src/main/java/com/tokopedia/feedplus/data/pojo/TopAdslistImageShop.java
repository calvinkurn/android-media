package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdslistImageShop {
  @SerializedName("cover_ecs")
  @Expose
  private String coverEcs;

  @SerializedName("s_ecs")
  @Expose
  private String sEcs;

  @SerializedName("xs_ecs")
  @Expose
  private String xsEcs;

  @SerializedName("s_url")
  @Expose
  private String sUrl;

  @SerializedName("xs_url")
  @Expose
  private String xsUrl;

  public void setCoverEcs(String coverEcs) {
    this.coverEcs = coverEcs;
  }

  public void setsEcs(String sEcs) {
    this.sEcs = sEcs;
  }

  public void setXsEcs(String xsEcs) {
    this.xsEcs = xsEcs;
  }

  public void setsUrl(String sUrl) {
    this.sUrl = sUrl;
  }

  public void setXsUrl(String xsUrl) {
    this.xsUrl = xsUrl;
  }

  public String getCoverEcs() {
    return this.coverEcs;
  }

  public String getsEcs() {
    return this.sEcs;
  }

  public String getXsEcs() {
    return this.xsEcs;
  }

  public String getsUrl() {
    return this.sUrl;
  }

  public String getXsUrl() {
    return this.xsUrl;
  }
}
