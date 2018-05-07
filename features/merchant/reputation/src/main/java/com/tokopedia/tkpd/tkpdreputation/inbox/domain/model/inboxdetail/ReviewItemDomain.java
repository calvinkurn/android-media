package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

/**
 * @author by nisie on 8/19/17.
 */

public class ReviewItemDomain {
    private ProductDataDomain productData;
    private int reviewInboxId;
    private int reviewId;
    private boolean reviewHasReviewed;
    private boolean reviewIsSkippable;
    private boolean reviewIsSkipped;
    private boolean reviewIsEditable;
    private ReviewDataDomain reviewData;

    public ReviewItemDomain(ProductDataDomain productData, int reviewInboxId,
                            int reviewId, boolean reviewHasReviewed,
                            boolean reviewIsSkippable, boolean reviewIsSkipped,
                            boolean reviewIsEditable, ReviewDataDomain reviewData) {
        this.productData = productData;
        this.reviewInboxId = reviewInboxId;
        this.reviewId = reviewId;
        this.reviewHasReviewed = reviewHasReviewed;
        this.reviewIsSkippable = reviewIsSkippable;
        this.reviewIsSkipped = reviewIsSkipped;
        this.reviewIsEditable = reviewIsEditable;
        this.reviewData = reviewData;
    }

    public ProductDataDomain getProductData() {
        return productData;
    }

    public int getReviewInboxId() {
        return reviewInboxId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public boolean isReviewHasReviewed() {
        return reviewHasReviewed;
    }

    public boolean isReviewIsSkippable() {
        return reviewIsSkippable;
    }

    public boolean isReviewIsSkipped() {
        return reviewIsSkipped;
    }

    public boolean isReviewIsEditable() {
        return reviewIsEditable;
    }

    public ReviewDataDomain getReviewData() {
        return reviewData;
    }
}
