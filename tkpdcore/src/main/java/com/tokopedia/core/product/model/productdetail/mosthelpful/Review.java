
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("review_id")
    @Expose
    private Integer reviewId;
    @SerializedName("reputation_id")
    @Expose
    private Integer reputationId;
    @SerializedName("review_title")
    @Expose
    private String reviewTitle;
    @SerializedName("review_message")
    @Expose
    private String reviewMessage;
    @SerializedName("product_rating")
    @Expose
    private Integer productRating;
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
    private java.util.List<Object> reviewImageAttachment = null;
    @SerializedName("review_anonymous")
    @Expose
    private Integer reviewAnonymous;
    @SerializedName("review_response")
    @Expose
    private ReviewResponse reviewResponse;
    @SerializedName("user")
    @Expose
    private User user;

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getReputationId() {
        return reputationId;
    }

    public void setReputationId(Integer reputationId) {
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

    public Integer getProductRating() {
        return productRating;
    }

    public void setProductRating(Integer productRating) {
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

    public java.util.List<Object> getReviewImageAttachment() {
        return reviewImageAttachment;
    }

    public void setReviewImageAttachment(java.util.List<Object> reviewImageAttachment) {
        this.reviewImageAttachment = reviewImageAttachment;
    }

    public Integer getReviewAnonymous() {
        return reviewAnonymous;
    }

    public void setReviewAnonymous(Integer reviewAnonymous) {
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

}
