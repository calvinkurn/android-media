package com.tokopedia.kol.feature.post.data.pojo;

import android.annotation.SuppressLint;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class TagsFeedKol {
  @SuppressLint("Invalid Data Type")
  @SerializedName("id")
  @Expose
  public String id;

  @SerializedName("type")
  @Expose
  public String type;

  @SerializedName("link")
  @Expose
  public String link;

  @SuppressLint("Invalid Data Type")
  @SerializedName("price")
  @Expose
  public String price;

  @SerializedName("url")
  @Expose
  public String url;

  @SerializedName("caption")
  @Expose
  public String caption;
}
