
package com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    public static final int INACTIVE_LIKE_DISLIKE = -1;
    public static final int MAX_STAR = 5;
    public static final int DIVIDER_MAX_STAR = 20;
    @SerializedName("review_id")
    @Expose
    private int reviewId;
    @SerializedName("reputation_id")
    @Expose
    private int reputationId;
    @SerializedName("review_title")
    @Expose
    private String reviewTitle;
    @SerializedName("review_message")
    @Expose
    private String reviewMessage;
    @SerializedName("product_rating")
    @Expose
    private int productRating;
    @SerializedName("product_rating_description")
    @Expose
    private String productRatingDescription;
    @SerializedName("review_create_time")
    @Expose
    private ReviewCreateTime reviewCreateTime;
    @SerializedName("review_update_time")
    @Expose
    private ReviewUpdateTime reviewUpdateTime;
    @SerializedName("review_image_attachment")
    @Expose
    private java.util.List<ReviewImageAttachment> reviewImageAttachment = null;
    @SerializedName("review_anonymous")
    @Expose
    private int reviewAnonymous;
    @SerializedName("review_response")
    @Expose
    private ReviewResponse reviewResponse;
    @SerializedName("user")
    @Expose
    private User user;
    private int totalLike = INACTIVE_LIKE_DISLIKE;
    private int likeStatus;

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getReputationId() {
        return reputationId;
    }

    public void setReputationId(int reputationId) {
        this.reputationId = reputationId;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public int getProductRating() {
        return productRating;
    }

    public void setProductRating(int productRating) {
        this.productRating = productRating;
    }

    public String getProductRatingDescription() {
        return productRatingDescription;
    }

    public void setProductRatingDescription(String productRatingDescription) {
        this.productRatingDescription = productRatingDescription;
    }

    public ReviewCreateTime getReviewCreateTime() {
        return reviewCreateTime;
    }

    public void setReviewCreateTime(ReviewCreateTime reviewCreateTime) {
        this.reviewCreateTime = reviewCreateTime;
    }

    public ReviewUpdateTime getReviewUpdateTime() {
        return reviewUpdateTime;
    }

    public void setReviewUpdateTime(ReviewUpdateTime reviewUpdateTime) {
        this.reviewUpdateTime = reviewUpdateTime;
    }

    public java.util.List<ReviewImageAttachment> getReviewImageAttachment() {
        return reviewImageAttachment;
    }

    public void setReviewImageAttachment(java.util.List<ReviewImageAttachment> reviewImageAttachment) {
        this.reviewImageAttachment = reviewImageAttachment;
    }

    public int getReviewAnonymous() {
        return reviewAnonymous;
    }

    public void setReviewAnonymous(int reviewAnonymous) {
        this.reviewAnonymous = reviewAnonymous;
    }

    public ReviewResponse getReviewResponse() {
        return reviewResponse;
    }

    public void setReviewResponse(ReviewResponse reviewResponse) {
        this.reviewResponse = reviewResponse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public float getReviewStar() {
        if(productRating > MAX_STAR){
            return productRating / DIVIDER_MAX_STAR;
        }
        return productRating;
    }
}
