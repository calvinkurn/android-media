
package com.tokopedia.kol.feature.comment.data.pojo.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostKol {

    @SerializedName("id")
    @Expose
    private int id = 0;
    @SerializedName("headerTitle")
    @Expose
    private Object headerTitle;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("commentCount")
    @Expose
    private int commentCount;
    @SerializedName("likeCount")
    @Expose
    private int likeCount;
    @SerializedName("isLiked")
    @Expose
    private boolean isLiked;
    @SerializedName("isFollowed")
    @Expose
    private boolean isFollowed;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userPhoto")
    @Expose
    private String userPhoto;
    @SerializedName("userId")
    @Expose
    private int userId;
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
    private boolean showComment;
    @SerializedName("content")
    @Expose
    private List<Content> content = null;

    public PostKol() {
    }

    public PostKol(int id, Object headerTitle, String description, int commentCount,
                   int likeCount, boolean isLiked, boolean isFollowed, String userName,
                   String userPhoto, int userId, String userInfo, String userUrl, String createTime,
                   boolean showComment, List<Content> content) {
        this.id = id;
        this.headerTitle = headerTitle;
        this.description = description;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.isFollowed = isFollowed;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userId = userId;
        this.userInfo = userInfo;
        this.userUrl = userUrl;
        this.createTime = createTime;
        this.showComment = showComment;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public Object getHeaderTitle() {
        return headerTitle;
    }

    public String getDescription() {
        return description;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public boolean isShowComment() {
        return showComment;
    }

    public List<Content> getContent() {
        return content;
    }
}
