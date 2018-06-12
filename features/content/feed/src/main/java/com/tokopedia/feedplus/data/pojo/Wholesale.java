package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wholesale {
  @SerializedName("qty_min_fmt")
  @Expose
  private String qty_min_fmt;

  public void setQty_min_fmt(String qty_min_fmt) {
    this.qty_min_fmt = qty_min_fmt;
  }

  public String getQty_min_fmt() {
    return this.qty_min_fmt;
  }
}
