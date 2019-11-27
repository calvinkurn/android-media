
package com.tokopedia.kol.feature.comment.data.pojo.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("userID")
    @Expose
    private int userID;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userPhoto")
    @Expose
    private String userPhoto;
    @SerializedName("isKol")
    @Expose
    private boolean isKol;
    @SerializedName("isCommentOwner")
    @Expose
    private boolean isCommentOwner;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("userBadge")
    @Expose
    private String userBadges;
    @SerializedName("isShop")
    @Expose
    private boolean isShop;

    public Comment(int id, int userID, String userName, String userPhoto, boolean isKol, boolean
            isCommentOwner, String createTime, String comment, String userBadges, boolean isShop) {
        this.id = id;
        this.userID = userID;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.isKol = isKol;
        this.isCommentOwner = isCommentOwner;
        this.createTime = createTime;
        this.comment = comment;
        this.userBadges = userBadges;
        this.isShop = isShop;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public boolean isKol() {
        return isKol;
    }

    public boolean isCommentOwner() {
        return isCommentOwner;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getComment() {
        return comment;
    }

    public String getUserBadges() {
        return userBadges;
    }

    public boolean isShop() {
        return isShop;
    }
}
