package com.tokopedia.tkpdcontent.feature.profile.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class PostKol {
  @SerializedName("lastCursor")
  @Expose
  public String lastCursor;

  @SerializedName("data")
  @Expose
  public List<PostKolData> postKolData;

  @SerializedName("error")
  @Expose
  public String error;
}
