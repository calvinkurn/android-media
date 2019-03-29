package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feed {
  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("create_time")
  @Expose
  private String createTime;

  @SerializedName("type")
  @Expose
  private String type;

  @SerializedName("cursor")
  @Expose
  private String cursor;

  @SerializedName("allow_report")
  @Expose
  private boolean allowReport;

  @SerializedName("source")
  @Expose
  private FeedSource source;

  @SerializedName("content")
  @Expose
  private FeedContent content;

  @SerializedName("meta")
  @Expose
  private FeedMeta meta;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getCursor() {
    return cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }

  public boolean isAllowReport() {
    return allowReport;
  }

  public void setAllowReport(boolean allowReport) {
    this.allowReport = allowReport;
  }

  public FeedSource getSource() {
    return source;
  }

  public void setSource(FeedSource source) {
    this.source = source;
  }

  public FeedContent getContent() {
    return content;
  }

  public void setContent(FeedContent content) {
    this.content = content;
  }

  public FeedMeta getMeta() {
    return meta;
  }

  public void setMeta(FeedMeta meta) {
    this.meta = meta;
  }
}
