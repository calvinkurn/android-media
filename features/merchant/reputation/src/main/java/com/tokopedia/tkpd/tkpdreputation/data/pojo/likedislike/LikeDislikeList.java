
package com.tokopedia.tkpd.tkpdreputation.data.pojo.likedislike;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeDislikeList {

    @SerializedName("review_id")
    @Expose
    private int reviewId;
    @SerializedName("total_like")
    @Expose
    private int totalLike;
    @SerializedName("total_dislike")
    @Expose
    private int totalDislike;
    @SerializedName("like_status")
    @Expose
    private int likeStatus;

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalDislike() {
        return totalDislike;
    }

    public void setTotalDislike(int totalDislike) {
        this.totalDislike = totalDislike;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

}
