package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdslistImage {
  @SerializedName("m_url")
  @Expose
  private String m_url;

  @SerializedName("s_url")
  @Expose
  private String s_url;

  @SerializedName("xs_url")
  @Expose
  private String xs_url;

  @SerializedName("m_ecs")
  @Expose
  private String m_ecs;

  @SerializedName("s_ecs")
  @Expose
  private String s_ecs;

  @SerializedName("xs_ecs")
  @Expose
  private String xs_ecs;

  public void setM_url(String m_url) {
    this.m_url = m_url;
  }

  public void setS_url(String s_url) {
    this.s_url = s_url;
  }

  public void setXs_url(String xs_url) {
    this.xs_url = xs_url;
  }

  public void setM_ecs(String m_ecs) {
    this.m_ecs = m_ecs;
  }

  public void setS_ecs(String s_ecs) {
    this.s_ecs = s_ecs;
  }

  public void setXs_ecs(String xs_ecs) {
    this.xs_ecs = xs_ecs;
  }

  public String getM_url() {
    return this.m_url;
  }

  public String getS_url() {
    return this.s_url;
  }

  public String getXs_url() {
    return this.xs_url;
  }

  public String getM_ecs() {
    return this.m_ecs;
  }

  public String getS_ecs() {
    return this.s_ecs;
  }

  public String getXs_ecs() {
    return this.xs_ecs;
  }
}
