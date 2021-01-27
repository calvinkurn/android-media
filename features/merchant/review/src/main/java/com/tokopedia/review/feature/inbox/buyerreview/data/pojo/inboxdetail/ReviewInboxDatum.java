
package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewInboxDatum {

    @SerializedName("product_data")
    @Expose
    private ProductData productData;
    @SerializedName("review_inbox_id")
    @Expose
    private int reviewInboxId;
    @SerializedName("review_id")
    @Expose
    private int reviewId;
    @SerializedName("review_has_reviewed")
    @Expose
    private boolean reviewHasReviewed;
    @SerializedName("review_is_skippable")
    @Expose
    private boolean reviewIsSkippable;
    @SerializedName("review_is_skipped")
    @Expose
    private boolean reviewIsSkipped;
    @SerializedName("review_is_editable")
    @Expose
    private boolean reviewIsEditable;
    @SerializedName("review_data")
    @Expose
    private ReviewData reviewData;

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public int getReviewInboxId() {
        return reviewInboxId;
    }

    public void setReviewInboxId(int reviewInboxId) {
        this.reviewInboxId = reviewInboxId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public boolean isReviewHasReviewed() {
        return reviewHasReviewed;
    }

    public void setReviewHasReviewed(boolean reviewHasReviewed) {
        this.reviewHasReviewed = reviewHasReviewed;
    }

    public boolean isReviewIsSkippable() {
        return reviewIsSkippable;
    }

    public void setReviewIsSkippable(boolean reviewIsSkippable) {
        this.reviewIsSkippable = reviewIsSkippable;
    }

    public boolean isReviewIsSkipped() {
        return reviewIsSkipped;
    }

    public void setReviewIsSkipped(boolean reviewIsSkipped) {
        this.reviewIsSkipped = reviewIsSkipped;
    }

    public boolean isReviewIsEditable() {
        return reviewIsEditable;
    }

    public void setReviewIsEditable(boolean reviewIsEditable) {
        this.reviewIsEditable = reviewIsEditable;
    }

    public ReviewData getReviewData() {
        return reviewData;
    }

    public void setReviewData(ReviewData reviewData) {
        this.reviewData = reviewData;
    }

}
