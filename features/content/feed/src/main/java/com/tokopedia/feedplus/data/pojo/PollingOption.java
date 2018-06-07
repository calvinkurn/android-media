package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PollingOption {
  @SerializedName("option_id")
  @Expose
  private Integer option_id;

  @SerializedName("option")
  @Expose
  private String option;

  @SerializedName("voter")
  @Expose
  private Integer voter;

  @SerializedName("percentage")
  @Expose
  private Integer percentage;

  @SerializedName("weblink")
  @Expose
  private String weblink;

  @SerializedName("applink")
  @Expose
  private String applink;

  @SerializedName("is_selected")
  @Expose
  private Boolean is_selected;

  @SerializedName("image_option")
  @Expose
  private String image_option;

  public void setOption_id(Integer option_id) {
    this.option_id = option_id;
  }

  public void setOption(String option) {
    this.option = option;
  }

  public void setVoter(Integer voter) {
    this.voter = voter;
  }

  public void setPercentage(Integer percentage) {
    this.percentage = percentage;
  }

  public void setWeblink(String weblink) {
    this.weblink = weblink;
  }

  public void setApplink(String applink) {
    this.applink = applink;
  }

  public void setIs_selected(Boolean is_selected) {
    this.is_selected = is_selected;
  }

  public void setImage_option(String image_option) {
    this.image_option = image_option;
  }

  public Integer getOption_id() {
    return this.option_id;
  }

  public String getOption() {
    return this.option;
  }

  public Integer getVoter() {
    return this.voter;
  }

  public Integer getPercentage() {
    return this.percentage;
  }

  public String getWeblink() {
    return this.weblink;
  }

  public String getApplink() {
    return this.applink;
  }

  public Boolean getIs_selected() {
    return this.is_selected;
  }

  public String getImage_option() {
    return this.image_option;
  }
}
