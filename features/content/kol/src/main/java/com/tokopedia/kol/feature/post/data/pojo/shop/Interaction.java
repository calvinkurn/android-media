
package com.tokopedia.kol.feature.post.data.pojo.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Interaction {
    @SerializedName("deleteable")
    @Expose
    private boolean deleteable;
    @SerializedName("editable")
    @Expose
    private boolean editable;
    @SerializedName("isLiked")
    @Expose
    private boolean isLiked;
    @SerializedName("commentCount")
    @Expose
    private int commentCount;
    @SerializedName("likeCount")
    @Expose
    private int likeCount;
    @SerializedName("showComment")
    @Expose
    private boolean showComment;
    @SerializedName("showLike")
    @Expose
    private boolean showLike;

    public boolean isDeleteable() {
        return deleteable;
    }

    public void setDeleteable(boolean deleteable) {
        this.deleteable = deleteable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
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

    public boolean isShowComment() {
        return showComment;
    }

    public void setShowComment(boolean showComment) {
        this.showComment = showComment;
    }

    public boolean isShowLike() {
        return showLike;
    }

    public void setShowLike(boolean showLike) {
        this.showLike = showLike;
    }
}
