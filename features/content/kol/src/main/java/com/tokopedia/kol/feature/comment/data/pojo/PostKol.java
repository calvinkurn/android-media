
package com.tokopedia.kol.feature.comment.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostKol {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("headerTitle")
    @Expose
    private Object headerTitle;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("commentCount")
    @Expose
    private Integer commentCount;
    @SerializedName("likeCount")
    @Expose
    private Integer likeCount;
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
    private Integer userId;
    @SerializedName("userInfo")
    @Expose
    private String userInfo;
    @SerializedName("userUrl")
    @Expose
    private String userUrl;
    @SerializedName("createTime")
    @Expose
    private String createTime;
    @SerializedName("showComment")
    @Expose
    private Boolean showComment;
    @SerializedName("content")
    @Expose
    private List<Content> content = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(Object headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public Boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Boolean getShowComment() {
        return showComment;
    }

    public void setShowComment(Boolean showComment) {
        this.showComment = showComment;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

}
