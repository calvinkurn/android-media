package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relation {
  @SerializedName("isFollowed")
  @Expose
  private Boolean isFollowed;

  @SerializedName("isLiked")
  @Expose
  private Boolean isLiked;

  public void setIsFollowed(Boolean isFollowed) {
    this.isFollowed = isFollowed;
  }

  public void setIsLiked(Boolean isLiked) {
    this.isLiked = isLiked;
  }

  public Boolean getIsFollowed() {
    return this.isFollowed;
  }

  public Boolean getIsLiked() {
    return this.isLiked;
  }
}
