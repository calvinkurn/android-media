package com.tokopedia.kol.feature.post.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class PostKolContent {
  @SerializedName("imageurl")
  @Expose
  public String imageurl;

  @SerializedName("tags")
  @Expose
  public List<TagsFeedKol> tags;
}
