package com.tokopedia.tkpdcontent.feature.profile.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class ProfileKolData {
  @SerializedName("profileKol")
  @Expose
  public ProfileKolType profileKol;

  @SerializedName("lastCursor")
  @Expose
  public String lastCursor;

  @SerializedName("postKol")
  @Expose
  public List<PostKol> postKol;

  @SerializedName("error")
  @Expose
  public String error;
}
