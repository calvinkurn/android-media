package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feed {
  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("create_time")
  @Expose
  private String create_time;

  @SerializedName("type")
  @Expose
  private String type;

  @SerializedName("cursor")
  @Expose
  private String cursor;

  @SerializedName("source")
  @Expose
  private FeedSource source;

  @SerializedName("content")
  @Expose
  private FeedContent content;

  public void setId(String id) {
    this.id = id;
  }

  public void setCreate_time(String create_time) {
    this.create_time = create_time;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }

  public void setSource(FeedSource source) {
    this.source = source;
  }

  public void setContent(FeedContent content) {
    this.content = content;
  }

  public String getId() {
    return this.id;
  }

  public String getCreate_time() {
    return this.create_time;
  }

  public String getType() {
    return this.type;
  }

  public String getCursor() {
    return this.cursor;
  }

  public FeedSource getSource() {
    return this.source;
  }

  public FeedContent getContent() {
    return this.content;
  }
}
