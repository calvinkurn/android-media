package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusActivity {
  @SerializedName("source")
  @Expose
  private String source;

  @SerializedName("activity")
  @Expose
  private String activity;

  @SerializedName("amount")
  @Expose
  private Integer amount;

  public void setSource(String source) {
    this.source = source;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public String getSource() {
    return this.source;
  }

  public String getActivity() {
    return this.activity;
  }

  public Integer getAmount() {
    return this.amount;
  }
}
