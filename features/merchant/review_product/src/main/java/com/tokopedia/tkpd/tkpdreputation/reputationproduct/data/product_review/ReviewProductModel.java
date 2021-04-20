
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.ImageUpload;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

import java.util.ArrayList;
import java.util.List;

public class ReviewProductModel implements Parcelable{

    @SerializedName("review_update_time")
    @Expose
    private String reviewUpdateTime;
    @SerializedName("review_rate_accuracy_desc")
    @Expose
    private String reviewRateAccuracyDesc;
    @SerializedName("review_user_label_id")
    @Expose
    private int reviewUserLabelId;
    @SerializedName("review_user_name")
    @Expose
    private String reviewUserName;
    @SerializedName("review_user_reputation")
    @Expose
    private ReviewUserReputation reviewUserReputation;
    @SerializedName("review_rate_accuracy")
    @Expose
    private int reviewRateAccuracy;
    @SerializedName("review_message")
    @Expose
    private String reviewMessage;
    @SerializedName("review_rate_product_desc")
    @Expose
    private String reviewRateProductDesc;
    @SerializedName("review_rate_speed_desc")
    @Expose
    private String reviewRateSpeedDesc;
    @SerializedName("review_response")
    @Expose
    private ReviewResponse reviewResponse;
    @SerializedName("review_shop_id")
    @Expose
    private String reviewShopId;
    @SerializedName("review_reputation_id")
    @Expose
    private String reviewReputationId;
    @SerializedName("review_user_image")
    @Expose
    private String reviewUserImage;
    @SerializedName("review_user_label")
    @Expose
    private String reviewUserLabel;
    @SerializedName("review_create_time")
    @Expose
    private String reviewCreateTime;
    @SerializedName("review_id")
    @Expose
    private int reviewId;
    @SerializedName("review_product_owner")
    @Expose
    private ReviewProductOwner reviewProductOwner;
    @SerializedName("review_rate_service_desc")
    @Expose
    private String reviewRateServiceDesc;
    @SerializedName("review_rate_product")
    @Expose
    private int reviewRateProduct;
    @SerializedName("review_rate_speed")
    @Expose
    private String reviewRateSpeed;
    @SerializedName("review_rate_service")
    @Expose
    private String reviewRateService;
    @SerializedName("review_user_id")
    @Expose
    private String reviewUserId;
    @SerializedName("review_image_attachment")
    @Expose
    List<ReviewImageList> reviewImageList = new ArrayList<ReviewImageList>();

    public ReviewProductDetailModel detail;


    protected ReviewProductModel(Parcel in) {
        reviewUpdateTime = in.readString();
        reviewRateAccuracyDesc = in.readString();
        reviewUserLabelId = in.readInt();
        reviewUserName = in.readString();
        reviewUserReputation = in.readParcelable(ReviewUserReputation.class.getClassLoader());
        reviewRateAccuracy = in.readInt();
        reviewMessage = in.readString();
        reviewRateProductDesc = in.readString();
        reviewRateSpeedDesc = in.readString();
        reviewResponse = in.readParcelable(ReviewResponse.class.getClassLoader());
        reviewShopId = in.readString();
        reviewReputationId = in.readString();
        reviewUserImage = in.readString();
        reviewUserLabel = in.readString();
        reviewCreateTime = in.readString();
        reviewId = in.readInt();
        reviewProductOwner = in.readParcelable(ReviewProductOwner.class.getClassLoader());
        reviewRateServiceDesc = in.readString();
        reviewRateProduct = in.readInt();
        reviewRateSpeed = in.readString();
        reviewRateService = in.readString();
        reviewUserId = in.readString();
        reviewImageList = in.createTypedArrayList(ReviewImageList.CREATOR);
        detail = in.readParcelable(ReviewProductDetailModel.class.getClassLoader());
    }

    public static final Creator<ReviewProductModel> CREATOR = new Creator<ReviewProductModel>() {
        @Override
        public ReviewProductModel createFromParcel(Parcel in) {
            return new ReviewProductModel(in);
        }

        @Override
        public ReviewProductModel[] newArray(int size) {
            return new ReviewProductModel[size];
        }
    };

    public List<ReviewImageList> getReviewImageList() {
        return reviewImageList;
    }

    public void setReviewImageList(List<ReviewImageList> reviewImageList) {
        this.reviewImageList = reviewImageList;
    }

    /**
     * 
     * @return
     *     The reviewUpdateTime
     */
    public String getReviewUpdateTime() {
        return reviewUpdateTime;
    }

    /**
     * 
     * @param reviewUpdateTime
     *     The review_update_time
     */
    public void setReviewUpdateTime(String reviewUpdateTime) {
        this.reviewUpdateTime = reviewUpdateTime;
    }

    /**
     * 
     * @return
     *     The reviewRateAccuracyDesc
     */
    public String getReviewRateAccuracyDesc() {
        return reviewRateAccuracyDesc;
    }

    /**
     * 
     * @param reviewRateAccuracyDesc
     *     The review_rate_accuracy_desc
     */
    public void setReviewRateAccuracyDesc(String reviewRateAccuracyDesc) {
        this.reviewRateAccuracyDesc = reviewRateAccuracyDesc;
    }

    /**
     * 
     * @return
     *     The reviewUserLabelId
     */
    public int getReviewUserLabelId() {
        return reviewUserLabelId;
    }

    /**
     * 
     * @param reviewUserLabelId
     *     The review_user_label_id
     */
    public void setReviewUserLabelId(int reviewUserLabelId) {
        this.reviewUserLabelId = reviewUserLabelId;
    }

    /**
     * 
     * @return
     *     The reviewUserName
     */
    public String getReviewUserName() {
        return reviewUserName;
    }

    /**
     * 
     * @param reviewUserName
     *     The review_user_name
     */
    public void setReviewUserName(String reviewUserName) {
        this.reviewUserName = reviewUserName;
    }

    /**
     * 
     * @return
     *     The reviewUserReputation
     */
    public ReviewUserReputation getReviewUserReputation() {
        return reviewUserReputation;
    }

    /**
     * 
     * @param reviewUserReputation
     *     The review_user_reputation
     */
    public void setReviewUserReputation(ReviewUserReputation reviewUserReputation) {
        this.reviewUserReputation = reviewUserReputation;
    }

    /**
     * 
     * @return
     *     The reviewRateAccuracy
     */
    public int getReviewRateAccuracy() {
        return reviewRateAccuracy;
    }

    /**
     * 
     * @param reviewRateAccuracy
     *     The review_rate_accuracy
     */
    public void setReviewRateAccuracy(int reviewRateAccuracy) {
        this.reviewRateAccuracy = reviewRateAccuracy;
    }

    /**
     * 
     * @return
     *     The reviewMessage
     */
    public Spanned getReviewMessage() {
        return MethodChecker.fromHtml(reviewMessage);
    }

    /**
     * 
     * @param reviewMessage
     *     The review_message
     */
    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    /**
     * 
     * @return
     *     The reviewRateProductDesc
     */
    public String getReviewRateProductDesc() {
        return reviewRateProductDesc;
    }

    /**
     * 
     * @param reviewRateProductDesc
     *     The review_rate_product_desc
     */
    public void setReviewRateProductDesc(String reviewRateProductDesc) {
        this.reviewRateProductDesc = reviewRateProductDesc;
    }

    /**
     * 
     * @return
     *     The reviewRateSpeedDesc
     */
    public String getReviewRateSpeedDesc() {
        return reviewRateSpeedDesc;
    }

    /**
     * 
     * @param reviewRateSpeedDesc
     *     The review_rate_speed_desc
     */
    public void setReviewRateSpeedDesc(String reviewRateSpeedDesc) {
        this.reviewRateSpeedDesc = reviewRateSpeedDesc;
    }

    /**
     * 
     * @return
     *     The reviewResponse
     */
    public ReviewResponse getReviewResponse() {
        return reviewResponse;
    }

    /**
     * 
     * @param reviewResponse
     *     The review_response
     */
    public void setReviewResponse(ReviewResponse reviewResponse) {
        this.reviewResponse = reviewResponse;
    }

    /**
     * 
     * @return
     *     The reviewShopId
     */
    public String getReviewShopId() {
        return reviewShopId;
    }

    /**
     * 
     * @param reviewShopId
     *     The review_shop_id
     */
    public void setReviewShopId(String reviewShopId) {
        this.reviewShopId = reviewShopId;
    }

    /**
     * 
     * @return
     *     The reviewReputationId
     */
    public String getReviewReputationId() {
        return reviewReputationId;
    }

    /**
     * 
     * @param reviewReputationId
     *     The review_reputation_id
     */
    public void setReviewReputationId(String reviewReputationId) {
        this.reviewReputationId = reviewReputationId;
    }

    /**
     * 
     * @return
     *     The reviewUserImage
     */
    public String getReviewUserImage() {
        return reviewUserImage;
    }

    /**
     * 
     * @param reviewUserImage
     *     The review_user_image
     */
    public void setReviewUserImage(String reviewUserImage) {
        this.reviewUserImage = reviewUserImage;
    }

    /**
     * 
     * @return
     *     The reviewUserLabel
     */
    public String getReviewUserLabel() {
        return reviewUserLabel;
    }

    /**
     * 
     * @param reviewUserLabel
     *     The review_user_label
     */
    public void setReviewUserLabel(String reviewUserLabel) {
        this.reviewUserLabel = reviewUserLabel;
    }

    /**
     * 
     * @return
     *     The reviewCreateTime
     */
    public String getReviewCreateTime() {
        return reviewCreateTime;
    }

    /**
     * 
     * @param reviewCreateTime
     *     The review_create_time
     */
    public void setReviewCreateTime(String reviewCreateTime) {
        this.reviewCreateTime = reviewCreateTime;
    }

    /**
     * 
     * @return
     *     The reviewId
     */
    public int getReviewId() {
        return reviewId;
    }

    /**
     * 
     * @param reviewId
     *     The review_id
     */
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    /**
     * 
     * @return
     *     The reviewProductOwner
     */
    public ReviewProductOwner getReviewProductOwner() {
        return reviewProductOwner;
    }

    /**
     * 
     * @param reviewProductOwner
     *     The review_product_owner
     */
    public void setReviewProductOwner(ReviewProductOwner reviewProductOwner) {
        this.reviewProductOwner = reviewProductOwner;
    }

    /**
     * 
     * @return
     *     The reviewRateServiceDesc
     */
    public String getReviewRateServiceDesc() {
        return reviewRateServiceDesc;
    }

    /**
     * 
     * @param reviewRateServiceDesc
     *     The review_rate_service_desc
     */
    public void setReviewRateServiceDesc(String reviewRateServiceDesc) {
        this.reviewRateServiceDesc = reviewRateServiceDesc;
    }

    /**
     * 
     * @return
     *     The reviewRateProduct
     */
    public int getReviewRateProduct() {
        return reviewRateProduct;
    }

    /**
     * 
     * @param reviewRateProduct
     *     The review_rate_product
     */
    public void setReviewRateProduct(int reviewRateProduct) {
        this.reviewRateProduct = reviewRateProduct;
    }

    /**
     * 
     * @return
     *     The reviewRateSpeed
     */
    public String getReviewRateSpeed() {
        return reviewRateSpeed;
    }

    /**
     * 
     * @param reviewRateSpeed
     *     The review_rate_speed
     */
    public void setReviewRateSpeed(String reviewRateSpeed) {
        this.reviewRateSpeed = reviewRateSpeed;
    }

    /**
     * 
     * @return
     *     The reviewRateService
     */
    public String getReviewRateService() {
        return reviewRateService;
    }

    /**
     * 
     * @param reviewRateService
     *     The review_rate_service
     */
    public void setReviewRateService(String reviewRateService) {
        this.reviewRateService = reviewRateService;
    }

    /**
     * 
     * @return
     *     The reviewUserId
     */
    public String getReviewUserId() {
        return reviewUserId;
    }

    /**
     * 
     * @param reviewUserId
     *     The review_user_id
     */
    public void setReviewUserId(String reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    public ArrayList<ImageUpload> getImages() {
        ArrayList<ImageUpload> list = new ArrayList<>();
        for (ReviewImageList reviewImage : reviewImageList) {
            list.add(new ImageUpload(reviewImage.getImageUrl(),reviewImage.getImageUrlLarge(), reviewImage.getImageCaption(), reviewImage.getAttachmentId()));
        }
        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewUpdateTime);
        dest.writeString(reviewRateAccuracyDesc);
        dest.writeInt(reviewUserLabelId);
        dest.writeString(reviewUserName);
        dest.writeParcelable(reviewUserReputation, flags);
        dest.writeInt(reviewRateAccuracy);
        dest.writeString(reviewMessage);
        dest.writeString(reviewRateProductDesc);
        dest.writeString(reviewRateSpeedDesc);
        dest.writeParcelable(reviewResponse, flags);
        dest.writeString(reviewShopId);
        dest.writeString(reviewReputationId);
        dest.writeString(reviewUserImage);
        dest.writeString(reviewUserLabel);
        dest.writeString(reviewCreateTime);
        dest.writeInt(reviewId);
        dest.writeParcelable(reviewProductOwner, flags);
        dest.writeString(reviewRateServiceDesc);
        dest.writeInt(reviewRateProduct);
        dest.writeString(reviewRateSpeed);
        dest.writeString(reviewRateService);
        dest.writeString(reviewUserId);
        dest.writeTypedList(reviewImageList);
        dest.writeParcelable(detail, flags);
    }
}
