
package com.tokopedia.explore.domain.entity;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostKol {

    @SerializedName("isLiked")
    @Expose
    private boolean isLiked;
    @SerializedName("isFollow")
    @Expose
    private boolean isFollow;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("commentCount")
    @Expose
    private int commentCount;
    @SerializedName("likeCount")
    @Expose
    private int likeCount;
    @SerializedName("createTime")
    @Expose
    private String createTime;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("content")
    @Expose
    private List<Content> content = null;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userInfo")
    @Expose
    private String userInfo;
    @SerializedName("userIsFollow")
    @Expose
    private boolean userIsFollow;
    @SerializedName("userPhoto")
    @Expose
    private String userPhoto;
    @SerializedName("userUrl")
    @Expose
    private String userUrl;
    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("tracking")
    @Expose
    private List<Tracking> tracking = new ArrayList<>();

    public boolean isIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public boolean isIsFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isUserIsFollow() {
        return userIsFollow;
    }

    public void setUserIsFollow(boolean userIsFollow) {
        this.userIsFollow = userIsFollow;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Tracking> getTracking() {
        return tracking;
    }
}
