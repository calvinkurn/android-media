package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedPolling {
  @SerializedName("poll_id")
  @Expose
  private Integer poll_id;

  @SerializedName("title")
  @Expose
  private String title;

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("question")
  @Expose
  private String question;

  @SerializedName("commentcount")
  @Expose
  private Integer commentcount;

  @SerializedName("likecount")
  @Expose
  private Integer likecount;

  @SerializedName("create_time")
  @Expose
  private String create_time;

  @SerializedName("liked")
  @Expose
  private Boolean liked;

  @SerializedName("followed")
  @Expose
  private Boolean followed;

  @SerializedName("show_comment")
  @Expose
  private Boolean show_comment;

  @SerializedName("total_voter")
  @Expose
  private Integer total_voter;

  @SerializedName("is_answered")
  @Expose
  private Boolean is_answered;

  @SerializedName("userName")
  @Expose
  private String userName;

  @SerializedName("userPhoto")
  @Expose
  private String userPhoto;

  @SerializedName("userId")
  @Expose
  private Integer userId;

  @SerializedName("userInfo")
  @Expose
  private String userInfo;

  @SerializedName("userUrl")
  @Expose
  private String userUrl;

  @SerializedName("options")
  @Expose
  private List<PollingOption> options;

  public void setPoll_id(Integer poll_id) {
    this.poll_id = poll_id;
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

  public void setCommentcount(Integer commentcount) {
    this.commentcount = commentcount;
  }

  public void setLikecount(Integer likecount) {
    this.likecount = likecount;
  }

  public void setCreate_time(String create_time) {
    this.create_time = create_time;
  }

  public void setLiked(Boolean liked) {
    this.liked = liked;
  }

  public void setFollowed(Boolean followed) {
    this.followed = followed;
  }

  public void setShow_comment(Boolean show_comment) {
    this.show_comment = show_comment;
  }

  public void setTotal_voter(Integer total_voter) {
    this.total_voter = total_voter;
  }

  public void setIs_answered(Boolean is_answered) {
    this.is_answered = is_answered;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setUserPhoto(String userPhoto) {
    this.userPhoto = userPhoto;
  }

  public void setUserId(Integer userId) {
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

  public Integer getPoll_id() {
    return this.poll_id;
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

  public Integer getCommentcount() {
    return this.commentcount;
  }

  public Integer getLikecount() {
    return this.likecount;
  }

  public String getCreate_time() {
    return this.create_time;
  }

  public Boolean getLiked() {
    return this.liked;
  }

  public Boolean getFollowed() {
    return this.followed;
  }

  public Boolean getShow_comment() {
    return this.show_comment;
  }

  public Integer getTotal_voter() {
    return this.total_voter;
  }

  public Boolean getIs_answered() {
    return this.is_answered;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getUserPhoto() {
    return this.userPhoto;
  }

  public Integer getUserId() {
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
}
