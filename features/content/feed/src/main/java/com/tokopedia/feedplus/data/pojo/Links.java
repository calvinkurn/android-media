package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {
  @SerializedName("self")
  @Expose
  private String self;

  @SerializedName("pagination")
  @Expose
  private Feedpagination pagination;

  public void setSelf(String self) {
    this.self = self;
  }

  public void setPagination(Feedpagination pagination) {
    this.pagination = pagination;
  }

  public String getSelf() {
    return this.self;
  }

  public Feedpagination getPagination() {
    return this.pagination;
  }
}
