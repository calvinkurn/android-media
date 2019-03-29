package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdslistImage {
  @SerializedName("m_url")
  @Expose
  private String mUrl;

  @SerializedName("s_url")
  @Expose
  private String sUrl;

  @SerializedName("xs_url")
  @Expose
  private String xsUrl;

  @SerializedName("m_ecs")
  @Expose
  private String mEcs;

  @SerializedName("s_ecs")
  @Expose
  private String sEcs;

  @SerializedName("xs_ecs")
  @Expose
  private String xsEcs;

  public void setmUrl(String mUrl) {
    this.mUrl = mUrl;
  }

  public void setsUrl(String sUrl) {
    this.sUrl = sUrl;
  }

  public void setXsUrl(String xsUrl) {
    this.xsUrl = xsUrl;
  }

  public void setmEcs(String mEcs) {
    this.mEcs = mEcs;
  }

  public void setsEcs(String sEcs) {
    this.sEcs = sEcs;
  }

  public void setXsEcs(String xsEcs) {
    this.xsEcs = xsEcs;
  }

  public String getmUrl() {
    return this.mUrl;
  }

  public String getsUrl() {
    return this.sUrl;
  }

  public String getXsUrl() {
    return this.xsUrl;
  }

  public String getmEcs() {
    return this.mEcs;
  }

  public String getsEcs() {
    return this.sEcs;
  }

  public String getXsEcs() {
    return this.xsEcs;
  }
}
