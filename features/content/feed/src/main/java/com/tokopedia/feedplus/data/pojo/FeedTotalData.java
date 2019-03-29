package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedTotalData {
  @SerializedName("total_data")
  @Expose
  private Integer totalData;

  public void setTotalData(Integer totalData) {
    this.totalData = totalData;
  }

  public Integer getTotalData() {
    return this.totalData;
  }
}
