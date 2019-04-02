package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedsKolCta {
  @SerializedName("img_header")
  @Expose
  private String imgHeader;

  @SerializedName("title")
  @Expose
  private String title;

  @SerializedName("subtitle")
  @Expose
  private String subtitle;

  @SerializedName("button_text")
  @Expose
  private String buttonText;

  @SerializedName("click_url")
  @Expose
  private String clickUrl;

  @SerializedName("click_applink")
  @Expose
  private String clickApplink;

  public void setImgHeader(String imgHeader) {
    this.imgHeader = imgHeader;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public void setButtonText(String buttonText) {
    this.buttonText = buttonText;
  }

  public void setClickUrl(String clickUrl) {
    this.clickUrl = clickUrl;
  }

  public void setClickApplink(String clickApplink) {
    this.clickApplink = clickApplink;
  }

  public String getImgHeader() {
    return this.imgHeader;
  }

  public String getTitle() {
    return this.title;
  }

  public String getSubtitle() {
    return this.subtitle;
  }

  public String getButtonText() {
    return this.buttonText;
  }

  public String getClickUrl() {
    return this.clickUrl;
  }

  public String getClickApplink() {
    return this.clickApplink;
  }
}
