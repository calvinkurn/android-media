package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wholesale {
  @SerializedName("qty_min_fmt")
  @Expose
  private String qtyMinFmt;

  public void setQtyMinFmt(String qtyMinFmt) {
    this.qtyMinFmt = qtyMinFmt;
  }

  public String getQtyMinFmt() {
    return this.qtyMinFmt;
  }
}
