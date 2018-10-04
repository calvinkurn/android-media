package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KolRecommendedDataType {
  @SerializedName("index")
  @Expose
  private Integer index;

  @SerializedName("kols")
  @Expose
  private List<FeedKolRecommendedType> kols;

  @SerializedName("headerTitle")
  @Expose
  private String headerTitle;

  @SerializedName("exploreLink")
  @Expose
  private String exploreLink;

  @SerializedName("exploreText")
  @Expose
  private String exploreText;

  public void setIndex(Integer index) {
    this.index = index;
  }

  public void setKols(List<FeedKolRecommendedType> kols) {
    this.kols = kols;
  }

  public void setHeaderTitle(String headerTitle) {
    this.headerTitle = headerTitle;
  }

  public void setExploreLink(String exploreLink) {
    this.exploreLink = exploreLink;
  }

  public void setExploreText(String exploreText) {
    this.exploreText = exploreText;
  }

  public Integer getIndex() {
    return this.index;
  }

  public List<FeedKolRecommendedType> getKols() {
    return this.kols;
  }

  public String getHeaderTitle() {
    return this.headerTitle;
  }

  public String getExploreLink() {
    return this.exploreLink;
  }

  public String getExploreText() {
    return this.exploreText;
  }
}
