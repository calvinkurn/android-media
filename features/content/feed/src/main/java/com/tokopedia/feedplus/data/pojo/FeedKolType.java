package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedKolType {
  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("headerTitle")
  @Expose
  private String headerTitle;

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("commentCount")
  @Expose
  private Integer commentCount;

  @SerializedName("likeCount")
  @Expose
  private Integer likeCount;

  @SerializedName("showComment")
  @Expose
  private Boolean showComment;

  @SerializedName("showLike")
  @Expose
  private Boolean showLike;

  @SerializedName("isLiked")
  @Expose
  private Boolean isLiked;

  @SerializedName("isFollowed")
  @Expose
  private Boolean isFollowed;

  @SerializedName("userName")
  @Expose
  private String userName;

  @SerializedName("userPhoto")
  @Expose
  private String userPhoto;

  @SerializedName("userId")
  @Expose
  private String userId;

  @SerializedName("userInfo")
  @Expose
  private String userInfo;

  @SerializedName("userUrl")
  @Expose
  private String userUrl;

  @SerializedName("createTime")
  @Expose
  private String createTime;

  @SerializedName("content")
  @Expose
  private List<ContentFeedKol> content;

  public void setId(String id) {
    this.id = id;
  }

  public void setHeaderTitle(String headerTitle) {
    this.headerTitle = headerTitle;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public void setLikeCount(Integer likeCount) {
    this.likeCount = likeCount;
  }

  public void setShowComment(Boolean showComment) {
    this.showComment = showComment;
  }

  public void setShowLike(Boolean showLike) {
    this.showLike = showLike;
  }

  public void setIsLiked(Boolean isLiked) {
    this.isLiked = isLiked;
  }

  public void setIsFollowed(Boolean isFollowed) {
    this.isFollowed = isFollowed;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setUserPhoto(String userPhoto) {
    this.userPhoto = userPhoto;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setUserInfo(String userInfo) {
    this.userInfo = userInfo;
  }

  public void setUserUrl(String userUrl) {
    this.userUrl = userUrl;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public void setContent(List<ContentFeedKol> content) {
    this.content = content;
  }

  public String getId() {
    return this.id;
  }

  public String getHeaderTitle() {
    return this.headerTitle;
  }

  public String getDescription() {
    return this.description;
  }

  public Integer getCommentCount() {
    return this.commentCount;
  }

  public Integer getLikeCount() {
    return this.likeCount;
  }

  public Boolean getShowComment() {
    return this.showComment;
  }

  public Boolean getShowLike() {
    return showLike;
  }

  public Boolean getIsLiked() {
    return this.isLiked;
  }

  public Boolean getIsFollowed() {
    return this.isFollowed;
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

  public String getUserInfo() {
    return this.userInfo;
  }

  public String getUserUrl() {
    return this.userUrl;
  }

  public String getCreateTime() {
    return this.createTime;
  }

  public List<ContentFeedKol> getContent() {
    return this.content;
  }
}
