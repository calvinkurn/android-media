package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.List;

public class FeedPolling {
  @SerializedName("poll_id")
  @Expose
  private String pollId = "";

  @SerializedName("title")
  @Expose
  private String title;

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("question")
  @Expose
  private String question;

  @SerializedName("commentCount")
  @Expose
  private Integer commentCount;

  @SerializedName("likeCount")
  @Expose
  private Integer likeCount;

  @SerializedName("createTime")
  @Expose
  private String createTime;

  @SerializedName("liked")
  @Expose
  private Boolean liked;

  @SerializedName("followed")
  @Expose
  private Boolean followed;

  @SerializedName("showComment")
  @Expose
  private Boolean showComment;

  @SerializedName("showLike")
  @Expose
  private Boolean showLike;

  @SerializedName("totalVoter")
  @Expose
  private Integer totalVoter;

  @SerializedName("isAnswered")
  @Expose
  private Boolean isAnswered;

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

  @SerializedName("options")
  @Expose
  private List<PollingOption> options;

  @SerializedName("relation")
  @Expose
  private Relation relation;

  public void setPollId(String poll_id) {
    this.pollId = poll_id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public void setLikeCount(Integer likeCount) {
    this.likeCount = likeCount;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public void setLiked(Boolean liked) {
    this.liked = liked;
  }

  public void setFollowed(Boolean followed) {
    this.followed = followed;
  }

  public void setShowComment(Boolean showComment) {
    this.showComment = showComment;
  }

  public void setShowLike(Boolean showLike) {
    this.showLike = showLike;
  }

  public void setTotalVoter(Integer totalVoter) {
    this.totalVoter = totalVoter;
  }

  public void setIsAnswered(Boolean isAnswered) {
    this.isAnswered = isAnswered;
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

  public void setOptions(List<PollingOption> options) {
    this.options = options;
  }

  public void setRelation(Relation relation) {
    this.relation = relation;
  }

  public String getPollId() {
    return this.pollId;
  }

  public String getTitle() {
    return this.title;
  }

  public String getDescription() {
    return this.description;
  }

  public String getQuestion() {
    return this.question;
  }

  public Integer getCommentCount() {
    return this.commentCount;
  }

  public Integer getLikeCount() {
    return this.likeCount;
  }

  public String getCreateTime() {
    return this.createTime;
  }

  public Boolean getLiked() {
    return this.liked;
  }

  public Boolean getFollowed() {
    return this.followed;
  }

  public Boolean getShowComment() {
    return this.showComment;
  }

  public Boolean getShowLike() {
    return this.showLike;
  }

  public Integer getTotalVoter() {
    return this.totalVoter;
  }

  public Boolean getIsAnswered() {
    return this.isAnswered;
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

  public List<PollingOption> getOptions() {
    return this.options;
  }

  public Relation getRelation() {
    return this.relation;
  }
}
