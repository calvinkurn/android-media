package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Feeds {
  @SerializedName("links")
  @Expose
  private Links links;

  @SerializedName("meta")
  @Expose
  private FeedTotalData meta;

  @SerializedName("data")
  @Expose
  private List<Feed> data;

  @SerializedName("token")
  @Expose
  private String token;

  public void setLinks(Links links) {
    this.links = links;
  }

  public void setMeta(FeedTotalData meta) {
    this.meta = meta;
  }

  public void setData(List<Feed> data) {
    this.data = data;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Links getLinks() {
    return this.links;
  }

  public FeedTotalData getMeta() {
    return this.meta;
  }

  public List<Feed> getData() {
    return this.data;
  }

  public String getToken() {
    return this.token;
  }
}
