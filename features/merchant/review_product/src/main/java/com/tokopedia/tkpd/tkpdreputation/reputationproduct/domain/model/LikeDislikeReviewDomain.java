
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model;


public class LikeDislikeReviewDomain {

    private TotalLikeDislikeDomain totalLikeDislikeDomain;
    private int likeStatus;
    private String reviewId;

    public LikeDislikeReviewDomain(TotalLikeDislikeDomain totalLikeDislikeDomain, int likeStatus, String reviewId) {
        this.totalLikeDislikeDomain = totalLikeDislikeDomain;
        this.likeStatus = likeStatus;
        this.reviewId = reviewId;
    }

    public LikeDislikeReviewDomain() {
    }

    public TotalLikeDislikeDomain getTotalLikeDislikeDomain() {
        return totalLikeDislikeDomain;
    }

    public void setTotalLikeDislikeDomain(TotalLikeDislikeDomain totalLikeDislikeDomain) {
        this.totalLikeDislikeDomain = totalLikeDislikeDomain;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

}
