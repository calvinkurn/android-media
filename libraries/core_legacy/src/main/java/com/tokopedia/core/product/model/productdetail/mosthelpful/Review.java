
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Review implements Parcelable {

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
    private java.util.List<Object> reviewImageAttachment = null;
    @SerializedName("review_anonymous")
    @Expose
    private int reviewAnonymous;
    @SerializedName("review_response")
    @Expose
    private ReviewResponse reviewResponse;
    @SerializedName("user")
    @Expose
    private User user;

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

    public java.util.List<Object> getReviewImageAttachment() {
        return reviewImageAttachment;
    }

    public void setReviewImageAttachment(java.util.List<Object> reviewImageAttachment) {
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

    public Review() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.reviewId);
        dest.writeInt(this.reputationId);
        dest.writeString(this.reviewTitle);
        dest.writeString(this.reviewMessage);
        dest.writeInt(this.productRating);
        dest.writeString(this.productRatingDescription);
        dest.writeParcelable(this.reviewCreateTime, flags);
        dest.writeParcelable(this.reviewUpdateTime, flags);
        dest.writeList(this.reviewImageAttachment);
        dest.writeInt(this.reviewAnonymous);
        dest.writeParcelable(this.reviewResponse, flags);
        dest.writeParcelable(this.user, flags);
    }

    protected Review(Parcel in) {
        this.reviewId = in.readInt();
        this.reputationId = in.readInt();
        this.reviewTitle = in.readString();
        this.reviewMessage = in.readString();
        this.productRating = in.readInt();
        this.productRatingDescription = in.readString();
        this.reviewCreateTime = in.readParcelable(ReviewCreateTime.class.getClassLoader());
        this.reviewUpdateTime = in.readParcelable(ReviewUpdateTime.class.getClassLoader());
        this.reviewImageAttachment = new ArrayList<Object>();
        in.readList(this.reviewImageAttachment, Object.class.getClassLoader());
        this.reviewAnonymous = in.readInt();
        this.reviewResponse = in.readParcelable(ReviewResponse.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
