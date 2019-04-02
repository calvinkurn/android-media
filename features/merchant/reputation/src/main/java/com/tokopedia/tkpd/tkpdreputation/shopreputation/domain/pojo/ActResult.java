
package com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActResult implements Parcelable{

    @SerializedName("is_owner")
    @Expose
    int isOwner;
    @SerializedName("review_response")
    @Expose
    ReviewResponse reviewResponse;
    @SerializedName("reputation_review_counter")
    @Expose
    String reputationReviewCounter;
    @SerializedName("is_success")
    @Expose
    int isSuccess;
    @SerializedName("show_bookmark")
    @Expose
    int showBookmark;
    @SerializedName("review_id")
    @Expose
    String reviewId;
    @SerializedName("product_owner")
    @Expose
    ProductOwner productOwner;
    @SerializedName("feedback_id")
    @Expose
    int feedbackId;
    @SerializedName("post_key")
    @Expose
    String postKey;
    @SerializedName("message_error")
    @Expose
    String messageError;
    @SerializedName("attachment_id")
    @Expose
    String attachmentId;

    public ActResult(){}

    protected ActResult(Parcel in) {
        isOwner = in.readInt();
        reviewResponse = in.readParcelable(ReviewResponse.class.getClassLoader());
        reputationReviewCounter = in.readString();
        isSuccess = in.readInt();
        showBookmark = in.readInt();
        reviewId = in.readString();
        productOwner = in.readParcelable(ProductOwner.class.getClassLoader());
        feedbackId = in.readInt();
        postKey = in.readString();
        messageError = in.readString();
        attachmentId = in.readString();
    }

    public static final Creator<ActResult> CREATOR = new Creator<ActResult>() {
        @Override
        public ActResult createFromParcel(Parcel in) {
            return new ActResult(in);
        }

        @Override
        public ActResult[] newArray(int size) {
            return new ActResult[size];
        }
    };

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public ReviewResponse getReviewResponse() {
        return reviewResponse;
    }

    public void setReviewResponse(ReviewResponse reviewResponse) {
        this.reviewResponse = reviewResponse;
    }

    public String getReputationReviewCounter() {
        return reputationReviewCounter;
    }

    public void setReputationReviewCounter(String reputationReviewCounter) {
        this.reputationReviewCounter = reputationReviewCounter;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getShowBookmark() {
        return showBookmark;
    }

    public void setShowBookmark(int showBookmark) {
        this.showBookmark = showBookmark;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public ProductOwner getProductOwner() {
        return productOwner;
    }

    public void setProductOwner(ProductOwner productOwner) {
        this.productOwner = productOwner;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isOwner);
        dest.writeParcelable(reviewResponse, flags);
        dest.writeString(reputationReviewCounter);
        dest.writeInt(isSuccess);
        dest.writeInt(showBookmark);
        dest.writeString(reviewId);
        dest.writeParcelable(productOwner, flags);
        dest.writeInt(feedbackId);
        dest.writeString(postKey);
        dest.writeString(messageError);
        dest.writeString(attachmentId);
    }
}
