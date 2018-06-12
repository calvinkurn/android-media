package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedsKolCta {
  @SerializedName("img_header")
  @Expose
  private String img_header;

  @SerializedName("title")
  @Expose
  private String title;

  @SerializedName("subtitle")
  @Expose
  private String subtitle;

  @SerializedName("button_text")
  @Expose
  private String button_text;

  @SerializedName("click_url")
  @Expose
  private String click_url;

  @SerializedName("click_applink")
  @Expose
  private String click_applink;

  public void setImg_header(String img_header) {
    this.img_header = img_header;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public void setButton_text(String button_text) {
    this.button_text = button_text;
  }

  public void setClick_url(String click_url) {
    this.click_url = click_url;
  }

  public void setClick_applink(String click_applink) {
    this.click_applink = click_applink;
  }

  public String getImg_header() {
    return this.img_header;
  }

  public String getTitle() {
    return this.title;
  }

  public String getSubtitle() {
    return this.subtitle;
  }

  public String getButton_text() {
    return this.button_text;
  }

  public String getClick_url() {
    return this.click_url;
  }

  public String getClick_applink() {
    return this.click_applink;
  }
}
