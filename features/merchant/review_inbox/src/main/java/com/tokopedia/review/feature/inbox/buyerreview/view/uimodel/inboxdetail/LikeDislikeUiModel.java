package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail;

/**
 * @author by nisie on 9/29/17.
 */

public class LikeDislikeUiModel {

    private int reviewId;
    private int totalLike;
    private int totalDislike;
    private int likeStatus;

    public LikeDislikeUiModel(int reviewId, int totalLike, int totalDislike, int likeStatus) {
        this.reviewId = reviewId;
        this.totalLike = totalLike;
        this.totalDislike = totalDislike;
        this.likeStatus = likeStatus;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public int getTotalDislike() {
        return totalDislike;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }
}
