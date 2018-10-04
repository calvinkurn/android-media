package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TagsFeedKol {
  @SerializedName("id")
  @Expose
  private Integer id;

  @SerializedName("type")
  @Expose
  private String type;

  @SerializedName("link")
  @Expose
  private String link;

  @SerializedName("price")
  @Expose
  private String price;

  @SerializedName("url")
  @Expose
  private String url;

  @SerializedName("caption")
  @Expose
  private String caption;

  @SerializedName("captionInd")
  @Expose
  private String captionInd;

  @SerializedName("captionEng")
  @Expose
  private String captionEng;

  public void setId(Integer id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public void setCaptionInd(String captionInd) {
    this.captionInd = captionInd;
  }

  public void setCaptionEng(String captionEng) {
    this.captionEng = captionEng;
  }

  public Integer getId() {
    return this.id;
  }

  public String getType() {
    return this.type;
  }

  public String getLink() {
    return this.link;
  }

  public String getPrice() {
    return this.price;
  }

  public String getUrl() {
    return this.url;
  }

  public String getCaption() {
    return this.caption;
  }

  public String getCaptionInd() {
    return this.captionInd;
  }

  public String getCaptionEng() {
    return this.captionEng;
  }
}
