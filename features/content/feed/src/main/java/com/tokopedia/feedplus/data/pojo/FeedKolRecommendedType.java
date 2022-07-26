package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedKolRecommendedType {
  @SerializedName("userName")
  @Expose
  private String userName;

  @SerializedName("userPhoto")
  @Expose
  private String userPhoto;

  @SerializedName("userId")
  @Expose
  private String userId;

  @SerializedName("isFollowed")
  @Expose
  private Boolean isFollowed;

  @SerializedName("info")
  @Expose
  private String info;

  @SerializedName("url")
  @Expose
  private String url;

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setUserPhoto(String userPhoto) {
    this.userPhoto = userPhoto;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setIsFollowed(Boolean isFollowed) {
    this.isFollowed = isFollowed;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getUserPhoto() {
    return this.userPhoto;
  }

  public String getUserId() {
    return this.userId;
  }

  public Boolean getIsFollowed() {
    return this.isFollowed;
  }

  public String getInfo() {
    return this.info;
  }

  public String getUrl() {
    return this.url;
  }
}
