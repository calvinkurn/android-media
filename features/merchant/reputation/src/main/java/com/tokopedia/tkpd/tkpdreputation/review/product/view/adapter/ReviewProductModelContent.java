package com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ReviewProductModelContent implements ReviewProductModel, Parcelable {
    protected boolean reviewIsAnonymous;
    protected String reviewerName;
    protected String reviewerId;
    protected float reviewStar;
    protected String reviewMessage;
    protected String reviewTime;
    protected List<ImageAttachmentViewModel> reviewAttachment;
    protected String sellerName;
    protected String shopId;
    protected String reviewId;
    protected boolean reviewHasReplied;
    protected String responseMessage;
    protected String responseCreateTime;
    protected boolean sellerRepliedOwner;
    protected boolean reviewCanReported;
    protected String reputationId;
    protected String productId;
    protected boolean likeStatus;
    protected int totalLike;
    protected boolean isLogin;
    protected boolean isHelpful;

    @Override
    public int type(ReviewProductTypeFactoryAdapter typeFactory) {
        return typeFactory.type(this);
    }

    public void setReviewIsAnonymous(boolean reviewIsAnonymous) {
        this.reviewIsAnonymous = reviewIsAnonymous;
    }

    public boolean isHelpful() {
        return isHelpful;
    }

    public void setHelpful(boolean helpful) {
        isHelpful = helpful;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setReviewStar(float reviewStar) {
        this.reviewStar = reviewStar;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public void setReviewAttachment(List<ImageAttachmentViewModel> reviewAttachment) {
        this.reviewAttachment = reviewAttachment;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public void setReviewHasReplied(boolean reviewHasReplied) {
        this.reviewHasReplied = reviewHasReplied;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public void setResponseCreateTime(String responseCreateTime) {
        this.responseCreateTime = responseCreateTime;
    }

    public void setSellerRepliedOwner(boolean sellerRepliedOwner) {
        this.sellerRepliedOwner = sellerRepliedOwner;
    }

    public void setReviewCanReported(boolean reviewCanReported) {
        this.reviewCanReported = reviewCanReported;
    }

    public boolean isReviewIsAnonymous() {
        return reviewIsAnonymous;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public float getReviewStar() {
        return reviewStar;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public List<ImageAttachmentViewModel> getReviewAttachment() {
        return reviewAttachment;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getShopId() {
        return shopId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public boolean isReviewHasReplied() {
        return reviewHasReplied;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getResponseCreateTime() {
        return responseCreateTime;
    }

    public boolean isSellerRepliedOwner() {
        return sellerRepliedOwner;
    }

    public boolean isReviewCanReported() {
        return reviewCanReported;
    }

    public ReviewProductModelContent() {
    }

    public String getReputationId() {
        return reputationId;
    }

    public void setReputationId(String reputationId) {
        this.reputationId = reputationId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public boolean isLikeStatus() {
        return likeStatus;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.reviewIsAnonymous ? (byte) 1 : (byte) 0);
        dest.writeString(this.reviewerName);
        dest.writeString(this.reviewerId);
        dest.writeFloat(this.reviewStar);
        dest.writeString(this.reviewMessage);
        dest.writeString(this.reviewTime);
        dest.writeTypedList(this.reviewAttachment);
        dest.writeString(this.sellerName);
        dest.writeString(this.shopId);
        dest.writeString(this.reviewId);
        dest.writeByte(this.reviewHasReplied ? (byte) 1 : (byte) 0);
        dest.writeString(this.responseMessage);
        dest.writeString(this.responseCreateTime);
        dest.writeByte(this.sellerRepliedOwner ? (byte) 1 : (byte) 0);
        dest.writeByte(this.reviewCanReported ? (byte) 1 : (byte) 0);
        dest.writeString(this.reputationId);
        dest.writeString(this.productId);
        dest.writeByte(this.likeStatus ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalLike);
        dest.writeByte(this.isLogin ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isHelpful ? (byte) 1 : (byte) 0);
    }

    protected ReviewProductModelContent(Parcel in) {
        this.reviewIsAnonymous = in.readByte() != 0;
        this.reviewerName = in.readString();
        this.reviewerId = in.readString();
        this.reviewStar = in.readFloat();
        this.reviewMessage = in.readString();
        this.reviewTime = in.readString();
        this.reviewAttachment = in.createTypedArrayList(ImageAttachmentViewModel.CREATOR);
        this.sellerName = in.readString();
        this.shopId = in.readString();
        this.reviewId = in.readString();
        this.reviewHasReplied = in.readByte() != 0;
        this.responseMessage = in.readString();
        this.responseCreateTime = in.readString();
        this.sellerRepliedOwner = in.readByte() != 0;
        this.reviewCanReported = in.readByte() != 0;
        this.reputationId = in.readString();
        this.productId = in.readString();
        this.likeStatus = in.readByte() != 0;
        this.totalLike = in.readInt();
        this.isLogin = in.readByte() != 0;
        this.isHelpful = in.readByte() != 0;
    }

    public static final Creator<ReviewProductModelContent> CREATOR = new Creator<ReviewProductModelContent>() {
        @Override
        public ReviewProductModelContent createFromParcel(Parcel source) {
            return new ReviewProductModelContent(source);
        }

        @Override
        public ReviewProductModelContent[] newArray(int size) {
            return new ReviewProductModelContent[size];
        }
    };
}
