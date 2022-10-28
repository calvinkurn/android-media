package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PollingOption {
  @SerializedName("option_id")
  @Expose
  private String optionId;

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
  private Boolean isSelected;

  @SerializedName("image_option")
  @Expose
  private String imageOption;

  public void setOptionId(String optionId) {
    this.optionId = optionId;
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

  public void setIsSelected(Boolean isSelected) {
    this.isSelected = isSelected;
  }

  public void setImageOption(String imageOption) {
    this.imageOption = imageOption;
  }

  public String getOptionId() {
    return this.optionId;
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

  public Boolean getIsSelected() {
    return this.isSelected;
  }

  public String getImageOption() {
    return this.imageOption;
  }
}
