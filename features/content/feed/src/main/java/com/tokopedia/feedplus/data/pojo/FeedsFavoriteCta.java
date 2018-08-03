package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedsFavoriteCta {
  @SerializedName("title_en")
  @Expose
  private String titleEn;

  @SerializedName("title_id")
  @Expose
  private String titleId;

  @SerializedName("subtitle_en")
  @Expose
  private String subtitleEn;

  @SerializedName("subtitle_id")
  @Expose
  private String subtitleId;

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  public void setTitleId(String titleId) {
    this.titleId = titleId;
  }

  public void setSubtitleEn(String subtitleEn) {
    this.subtitleEn = subtitleEn;
  }

  public void setSubtitleId(String subtitleId) {
    this.subtitleId = subtitleId;
  }

  public String getTitleEn() {
    return this.titleEn;
  }

  public String getTitleId() {
    return this.titleId;
  }

  public String getSubtitleEn() {
    return this.subtitleEn;
  }

  public String getSubtitleId() {
    return this.subtitleId;
  }
}
