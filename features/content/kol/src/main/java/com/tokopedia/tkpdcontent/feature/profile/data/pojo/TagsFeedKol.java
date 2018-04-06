package com.tokopedia.tkpdcontent.feature.profile.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class TagsFeedKol {
  @SerializedName("id")
  @Expose
  public Integer id;

  @SerializedName("type")
  @Expose
  public String type;

  @SerializedName("link")
  @Expose
  public String link;

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
