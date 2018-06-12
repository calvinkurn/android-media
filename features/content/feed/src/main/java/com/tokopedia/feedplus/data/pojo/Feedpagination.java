package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feedpagination {
  @SerializedName("has_next_page")
  @Expose
  private Boolean has_next_page;

  public void setHas_next_page(Boolean has_next_page) {
    this.has_next_page = has_next_page;
  }

  public Boolean getHas_next_page() {
    return this.has_next_page;
  }
}
