package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedsFavoriteCta {
  @SerializedName("title_en")
  @Expose
  private String title_en;

  @SerializedName("title_id")
  @Expose
  private String title_id;

  @SerializedName("subtitle_en")
  @Expose
  private String subtitle_en;

  @SerializedName("subtitle_id")
  @Expose
  private String subtitle_id;

  public void setTitle_en(String title_en) {
    this.title_en = title_en;
  }

  public void setTitle_id(String title_id) {
    this.title_id = title_id;
  }

  public void setSubtitle_en(String subtitle_en) {
    this.subtitle_en = subtitle_en;
  }

  public void setSubtitle_id(String subtitle_id) {
    this.subtitle_id = subtitle_id;
  }

  public String getTitle_en() {
    return this.title_en;
  }

  public String getTitle_id() {
    return this.title_id;
  }

  public String getSubtitle_en() {
    return this.subtitle_en;
  }

  public String getSubtitle_id() {
    return this.subtitle_id;
  }
}
