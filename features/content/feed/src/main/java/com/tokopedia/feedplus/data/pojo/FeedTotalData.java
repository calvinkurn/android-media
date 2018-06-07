package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedTotalData {
  @SerializedName("total_data")
  @Expose
  private Integer total_data;

  public void setTotal_data(Integer total_data) {
    this.total_data = total_data;
  }

  public Integer getTotal_data() {
    return this.total_data;
  }
}
