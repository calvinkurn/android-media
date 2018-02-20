package com.tokopedia.tkpdcontent.feature.profile.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class ProfileKolType {
  @SerializedName("following")
  @Expose
  public Integer following;

  @SerializedName("followers")
  @Expose
  public Integer followers;

  @SerializedName("followed")
  @Expose
  public Boolean followed;

  @SerializedName("iskol")
  @Expose
  public Boolean iskol;

  @SerializedName("info")
  @Expose
  public String info;

  @SerializedName("bio")
  @Expose
  public String bio;

  @SerializedName("id")
  @Expose
  public Integer id;

  @SerializedName("name")
  @Expose
  public String name;

  @SerializedName("photo")
  @Expose
  public String photo;
}
