
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeDislikeReview {

    @SerializedName("total_like_dislike")
    @Expose
    private TotalLikeDislike totalLikeDislike;
    @SerializedName("like_status")
    @Expose
    private int likeStatus;
    @SerializedName("review_id")
    @Expose
    private String reviewId;

    /**
     * 
     * @return
     *     The totalLikeDislike
     */
    public TotalLikeDislike getTotalLikeDislike() {
        return totalLikeDislike;
    }

    /**
     * 
     * @param totalLikeDislike
     *     The total_like_dislike
     */
    public void setTotalLikeDislike(TotalLikeDislike totalLikeDislike) {
        this.totalLikeDislike = totalLikeDislike;
    }

    /**
     * 
     * @return
     *     The likeStatus
     */
    public int getLikeStatus() {
        return likeStatus;
    }

    /**
     * 
     * @param likeStatus
     *     The like_status
     */
    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    /**
     * 
     * @return
     *     The reviewId
     */
    public String getReviewId() {
        return reviewId;
    }

    /**
     * 
     * @param reviewId
     *     The review_id
     */
    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

}
