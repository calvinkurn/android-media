package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail;


import java.util.List;

/**
 * @author by nisie on 8/23/17.
 */

public class ReviewDataDomain {
    private int reviewId;
    private int reputationId;
    private String reviewTitle;
    private String reviewMessage;
    private int reviewRating;
    private List<ImageAttachmentDomain> reviewImageUrl = null;
    private ReviewCreateTimeDomain reviewCreateTime;
    private ReviewUpdateTimeDomain reviewUpdateTime;
    private boolean reviewAnonymity;
    private ReviewResponseDomain reviewResponse;

    public ReviewDataDomain(int reviewId, int reputationId, String reviewTitle,
                            String reviewMessage, int reviewRating,
                            List<ImageAttachmentDomain> reviewImageUrl,
                            ReviewCreateTimeDomain reviewCreateTime,
                            ReviewUpdateTimeDomain reviewUpdateTime,
                            boolean reviewAnonymity, ReviewResponseDomain reviewResponse) {
        this.reviewId = reviewId;
        this.reputationId = reputationId;
        this.reviewTitle = reviewTitle;
        this.reviewMessage = reviewMessage;
        this.reviewRating = reviewRating;
        this.reviewImageUrl = reviewImageUrl;
        this.reviewCreateTime = reviewCreateTime;
        this.reviewUpdateTime = reviewUpdateTime;
        this.reviewAnonymity = reviewAnonymity;
        this.reviewResponse = reviewResponse;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getReputationId() {
        return reputationId;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public List<ImageAttachmentDomain> getReviewImageUrl() {
        return reviewImageUrl;
    }

    public ReviewCreateTimeDomain getReviewCreateTime() {
        return reviewCreateTime;
    }

    public ReviewUpdateTimeDomain getReviewUpdateTime() {
        return reviewUpdateTime;
    }

    public boolean isReviewAnonymity() {
        return reviewAnonymity;
    }

    public ReviewResponseDomain getReviewResponse() {
        return reviewResponse;
    }
}
