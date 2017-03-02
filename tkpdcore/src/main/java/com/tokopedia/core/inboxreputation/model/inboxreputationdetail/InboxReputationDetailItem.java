
package com.tokopedia.core.inboxreputation.model.inboxreputationdetail;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.inboxreputation.model.ImageUpload;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;

public class InboxReputationDetailItem implements Parcelable {

    @SerializedName("product_id")
    @Expose
    String productId;
    @SerializedName("product_image")
    @Expose
    String productImageUrl;
    @SerializedName("product_name")
    @Expose
    String productName;
    @SerializedName("product_uri")
    @Expose
    String productUri;
    @SerializedName("create_time_fmt_ws")
    @Expose
    String reviewPostTime;
    @SerializedName("review_is_skipped")
    @Expose
    int isSkipped;
    @SerializedName("review_is_skipable")
    @Expose
    int isSkippable;
    @SerializedName("auto_read_review")
    @Expose
    int isRead;
    @SerializedName("review_is_allow_edit")
    @Expose
    int isEditable;
    @SerializedName("review_message")
    @Expose
    String reviewMessage;
    @SerializedName("review_image_attachment")
    @Expose
    java.util.List<ReviewImageList> reviewImageList = new ArrayList<ReviewImageList>();
    @SerializedName("review_response")
    @Expose
    ReviewResponse reviewResponse;
    @SerializedName("product_rating_point")
    @Expose
    int productQualityRating;
    @SerializedName("product_accuracy_point")
    @Expose
    int productAccuracyRating;
    @SerializedName("review_status")
    @Expose
    int reviewStatus;
    @SerializedName("review_update_time")
    @Expose
    String reviewUpdateTime;
    @SerializedName("shop_name")
    @Expose
    String shopName;
    @SerializedName("shop_id")
    @Expose
    String shopId;
    @SerializedName("shop_badge_level")
    @Expose
    ShopBadgeLevel shopBadgeLevel;
    @SerializedName("shop_domain")
    @Expose
    String shopDomain;
    @SerializedName("product_owner")
    @Expose
    ProductOwner productOwner;
    @SerializedName("reputation_inbox_id")
    @Expose
    int reputationInboxId;
    @SerializedName("user_url")
    @Expose
    String userUrl;
    @SerializedName("review_id")
    @Expose
    String reviewId;
    @SerializedName("product_is_banned")
    @Expose
    int productIsBanned;


    protected InboxReputationDetailItem(Parcel in) {
        productId = in.readString();
        productImageUrl = in.readString();
        productName = in.readString();
        productUri = in.readString();
        reviewPostTime = in.readString();
        isSkipped = in.readInt();
        isSkippable = in.readInt();
        isRead = in.readInt();
        isEditable = in.readInt();
        reviewMessage = in.readString();
        reviewImageList = in.createTypedArrayList(ReviewImageList.CREATOR);
        productQualityRating = in.readInt();
        productAccuracyRating = in.readInt();
        reviewStatus = in.readInt();
        reviewUpdateTime = in.readString();
        shopName = in.readString();
        shopId = in.readString();
        shopDomain = in.readString();
        reputationInboxId = in.readInt();
        userUrl = in.readString();
        reviewId = in.readString();
        productIsBanned = in.readInt();
    }

    public static final Creator<InboxReputationDetailItem> CREATOR = new Creator<InboxReputationDetailItem>() {
        @Override
        public InboxReputationDetailItem createFromParcel(Parcel in) {
            return new InboxReputationDetailItem(in);
        }

        @Override
        public InboxReputationDetailItem[] newArray(int size) {
            return new InboxReputationDetailItem[size];
        }
    };

    /**
     * @return The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId The product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return The productImageUrl
     */
    public String getProductImageUrl() {
        return productImageUrl;
    }

    /**
     * @param productImageUrl The product_image_url
     */
    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    /**
     * @return The productName
     */
    public String getProductName() {
        return MethodChecker.fromHtml(productName).toString();
    }

    /**
     * @param productName The product_name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return The productUri
     */
    public String getProductUri() {
        return productUri;
    }

    /**
     * @param productUri The product_uri
     */
    public void setProductUri(String productUri) {
        this.productUri = productUri;
    }

    /**
     * @return The reviewPostTime
     */
    public String getReviewPostTime() {
        return reviewPostTime;
    }

    /**
     * @param reviewPostTime The review_time
     */
    public void setReviewPostTime(String reviewPostTime) {
        this.reviewPostTime = reviewPostTime;
    }

    /**
     * @return The isSkipped
     */
    public boolean getIsSkipped() {
        return isSkipped == 1;
    }

    /**
     * @param isSkipped The is_skipped
     */
    public void setIsSkipped(int isSkipped) {
        this.isSkipped = isSkipped;
    }

    /**
     * @return The isSkippable
     */
    public boolean getIsSkippable() {
        return isSkippable == 1;
    }

    /**
     * @param isSkippable The is_skippable
     */
    public void setIsSkippable(int isSkippable) {
        this.isSkippable = isSkippable;
    }

    /**
     * @return The isRead
     */
    public boolean getIsRead() {
        return isRead == 0;
    }

    /**
     * @param isRead The is_read
     */
    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    /**
     * @return The isEditable
     */
    public boolean getIsEditable() {
        return isEditable == 1;
    }

    /**
     * @param isEditable The is_editable
     */
    public void setIsEditable(int isEditable) {
        this.isEditable = isEditable;
    }

    /**
     * @return The reviewMessage
     */
    public Spanned getReviewMessage() {
        return MethodChecker.fromHtml(reviewMessage);
    }

    /**
     * @param reviewMessage The review_message
     */
    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    /**
     * @return The reviewImageList
     */
    public java.util.List<ReviewImageList> getReviewImageList() {
        return reviewImageList;
    }

    /**
     * @param reviewImageList The review_image_list
     */
    public void setReviewImageList(java.util.List<ReviewImageList> reviewImageList) {
        this.reviewImageList = reviewImageList;
    }

    /**
     * @return The reviewResponse
     */
    public ReviewResponse getReviewResponse() {
        return reviewResponse;
    }

    /**
     * @param reviewResponse The review_response
     */
    public void setReviewResponse(ReviewResponse reviewResponse) {
        this.reviewResponse = reviewResponse;
    }

    /**
     * @return The productQualityRating
     */
    public int getProductQualityRating() {
        return productQualityRating;
    }

    /**
     * @param productQualityRating The product_quality_rating
     */
    public void setProductQualityRating(int productQualityRating) {
        this.productQualityRating = productQualityRating;
    }

    /**
     * @return The productAccuracyRating
     */
    public int getProductAccuracyRating() {
        return productAccuracyRating;
    }

    /**
     * @param productAccuracyRating The product_accuracy_rating
     */
    public void setProductAccuracyRating(int productAccuracyRating) {
        this.productAccuracyRating = productAccuracyRating;
    }

    /**
     * @return The reviewStatus
     */
    public int getReviewStatus() {
        return reviewStatus;
    }

    /**
     * @param reviewStatus The review_status
     */
    public void setReviewStatus(int reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    /**
     * @return The reviewUpdateTime
     */
    public String getReviewUpdateTime() {
        return reviewUpdateTime;
    }

    /**
     * @param reviewUpdateTime The review_update_time
     */
    public void setReviewUpdateTime(String reviewUpdateTime) {
        this.reviewUpdateTime = reviewUpdateTime;
    }

    /**
     * @return The shopName
     */
    public String getShopName() {
        return MethodChecker.fromHtml(shopName).toString();
    }

    /**
     * @param shopName The shop_name
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * @return The shopBadgeLevel
     */
    public ShopBadgeLevel getShopBadgeLevel() {
        return shopBadgeLevel;
    }

    /**
     * @param shopBadgeLevel The shop_badge_level
     */
    public void setShopBadgeLevel(ShopBadgeLevel shopBadgeLevel) {
        this.shopBadgeLevel = shopBadgeLevel;
    }

    /**
     * @return The shopDomain
     */
    public String getShopDomain() {
        return shopDomain;
    }

    /**
     * @param shopDomain The shop_domain
     */
    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    /**
     * @return The productOwner
     */
    public ProductOwner getProductOwner() {
        return productOwner;
    }

    /**
     * @param productOwner The product_owner
     */
    public void setProductOwner(ProductOwner productOwner) {
        this.productOwner = productOwner;
    }

    /**
     * @return The reputationInboxId
     */
    public int getReputationInboxId() {
        return reputationInboxId;
    }

    /**
     * @param reputationInboxId The reputation_inbox_id
     */
    public void setReputationInboxId(int reputationInboxId) {
        this.reputationInboxId = reputationInboxId;
    }

    /**
     * @return The userUrl
     */
    public String getUserUrl() {
        return userUrl;
    }

    /**
     * @param userUrl The user_url
     */
    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    /**
     * @return The reviewId
     */
    public String getReviewId() {
        return reviewId;
    }

    /**
     * @param reviewId The review_id
     */
    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    /**
     * @return The shopId
     */
    public String getShopId() {
        return shopId;
    }

    /**
     * @param shopId The shop_id
     */
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }


    /**
     * @return The productIsBanned
     */
    public Boolean isProductBanned() {
        return productIsBanned == 1;
    }

    /**
     * @param productIsBanned The product_is_banned
     */
    public void setProductIsBanned(int productIsBanned) {
        this.productIsBanned = productIsBanned;
    }


    public ArrayList<ImageUpload> getImages() {
        ArrayList<ImageUpload> list = new ArrayList<>();
        for (ReviewImageList reviewImage : reviewImageList) {
            list.add(new ImageUpload(reviewImage.getImageUrl(), reviewImage.getImageUrlLarge(), reviewImage.getImageCaption(), reviewImage.getAttachmentId()));
        }
        return list;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productImageUrl);
        dest.writeString(productName);
        dest.writeString(productUri);
        dest.writeString(reviewPostTime);
        dest.writeInt(isSkipped);
        dest.writeInt(isSkippable);
        dest.writeInt(isRead);
        dest.writeInt(isEditable);
        dest.writeString(reviewMessage);
        dest.writeTypedList(reviewImageList);
        dest.writeInt(productQualityRating);
        dest.writeInt(productAccuracyRating);
        dest.writeInt(reviewStatus);
        dest.writeString(reviewUpdateTime);
        dest.writeString(shopName);
        dest.writeString(shopId);
        dest.writeString(shopDomain);
        dest.writeInt(reputationInboxId);
        dest.writeString(userUrl);
        dest.writeString(reviewId);
        dest.writeInt(productIsBanned);
    }
}
