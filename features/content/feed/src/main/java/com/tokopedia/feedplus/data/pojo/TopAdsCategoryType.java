package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAdsCategoryType {
  @SerializedName("id")
  @Expose
  private String id;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return this.id;
  }
}
