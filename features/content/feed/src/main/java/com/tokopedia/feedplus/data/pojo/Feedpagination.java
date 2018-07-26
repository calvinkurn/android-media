package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feedpagination {
  @SerializedName("has_next_page")
  @Expose
  private Boolean hasNextPage;

  public void setHasNextPage(Boolean hasNextPage) {
    this.hasNextPage = hasNextPage;
  }

  public Boolean getHasNextPage() {
    return this.hasNextPage;
  }
}
