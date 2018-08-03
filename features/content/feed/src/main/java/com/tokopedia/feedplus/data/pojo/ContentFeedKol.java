package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContentFeedKol {
  @SerializedName("imageurl")
  @Expose
  private String imageurl;

  @SerializedName("tags")
  @Expose
  private List<TagsFeedKol> tags;

  public void setImageurl(String imageurl) {
    this.imageurl = imageurl;
  }

  public void setTags(List<TagsFeedKol> tags) {
    this.tags = tags;
  }

  public String getImageurl() {
    return this.imageurl;
  }

  public List<TagsFeedKol> getTags() {
    return this.tags;
  }
}
