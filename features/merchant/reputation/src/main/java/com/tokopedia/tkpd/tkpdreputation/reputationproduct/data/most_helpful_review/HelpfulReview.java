
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.most_helpful_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpfulReview implements Parcelable{

    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("rate_product_desc")
    @Expose
    private String rateProductDesc;
    @SerializedName("rate_accuracy")
    @Expose
    private int rateAccuracy;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("rate_product")
    @Expose
    private int rateProduct;
    @SerializedName("shop_data")
    @Expose
    private ShopData shopData;
    @SerializedName("reviewer_data")
    @Expose
    private ReviewerData reviewerData;
    @SerializedName("reputation_id")
    @Expose
    private String reputationId;
    @SerializedName("review_like_dislike")
    @Expose
    private ReviewLikeDislike reviewLikeDislike;
    @SerializedName("rate_speed")
    @Expose
    private String rateSpeed;
    @SerializedName("rate_accuract_desc")
    @Expose
    private String rateAccuractDesc;
    @SerializedName("feedback_time")
    @Expose
    private String feedbackTime;
    @SerializedName("rate_service")
    @Expose
    private String rateService;
    @SerializedName("rate_service_desc")
    @Expose
    private String rateServiceDesc;
    @SerializedName("rate_speed_desc")
    @Expose
    private String rateSpeedDesc;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("feedback_update_time")
    @Expose
    private String feedbackUpdateTime;
    @SerializedName("feedback_time_fmt")
    @Expose
    private String feedbackTimeFmt;
    @SerializedName("review_content")
    @Expose
    private String reviewContent;
    @SerializedName("feedback_id")
    @Expose
    private String feedbackId;
    @SerializedName("shop_id")
    @Expose
    private String shopId;

    protected HelpfulReview(Parcel in) {
        rateProductDesc = in.readString();
        rateAccuracy = in.readInt();
        productId = in.readString();
        rateProduct = in.readInt();
        reputationId = in.readString();
        rateSpeed = in.readString();
        rateAccuractDesc = in.readString();
        feedbackTime = in.readString();
        rateService = in.readString();
        rateServiceDesc = in.readString();
        rateSpeedDesc = in.readString();
        feedbackUpdateTime = in.readString();
        feedbackTimeFmt = in.readString();
        reviewContent = in.readString();
        feedbackId = in.readString();
        shopId = in.readString();
        shopData = (ShopData)in.readValue(ShopData.class.getClassLoader());
        response = (Response) in.readValue(Response.class.getClassLoader());
        reviewerData = (ReviewerData)in.readValue(ReviewerData.class.getClassLoader());
        reviewLikeDislike = (ReviewLikeDislike)in.readValue(ReviewLikeDislike.class.getClassLoader());
    }

    public static final Creator<HelpfulReview> CREATOR = new Creator<HelpfulReview>() {
        @Override
        public HelpfulReview createFromParcel(Parcel in) {
            return new HelpfulReview(in);
        }

        @Override
        public HelpfulReview[] newArray(int size) {
            return new HelpfulReview[size];
        }
    };

    /**
     * 
     * @return
     *     The response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * 
     * @param response
     *     The response
     */
    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * 
     * @return
     *     The rateProductDesc
     */
    public String getRateProductDesc() {
        return rateProductDesc;
    }

    /**
     * 
     * @param rateProductDesc
     *     The rate_product_desc
     */
    public void setRateProductDesc(String rateProductDesc) {
        this.rateProductDesc = rateProductDesc;
    }

    /**
     * 
     * @return
     *     The rateAccuracy
     */
    public int getRateAccuracy() {
        return rateAccuracy;
    }

    /**
     * 
     * @param rateAccuracy
     *     The rate_accuracy
     */
    public void setRateAccuracy(int rateAccuracy) {
        this.rateAccuracy = rateAccuracy;
    }

    /**
     * 
     * @return
     *     The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * 
     * @param productId
     *     The product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * 
     * @return
     *     The rateProduct
     */
    public int getRateProduct() {
        return rateProduct;
    }

    /**
     * 
     * @param rateProduct
     *     The rate_product
     */
    public void setRateProduct(int rateProduct) {
        this.rateProduct = rateProduct;
    }

    /**
     * 
     * @return
     *     The shopData
     */
    public ShopData getShopData() {
        return shopData;
    }

    /**
     * 
     * @param shopData
     *     The shop_data
     */
    public void setShopData(ShopData shopData) {
        this.shopData = shopData;
    }

    /**
     * 
     * @return
     *     The reviewerData
     */
    public ReviewerData getReviewerData() {
        return reviewerData;
    }

    /**
     * 
     * @param reviewerData
     *     The reviewer_data
     */
    public void setReviewerData(ReviewerData reviewerData) {
        this.reviewerData = reviewerData;
    }

    /**
     * 
     * @return
     *     The reputationId
     */
    public String getReputationId() {
        return reputationId;
    }

    /**
     * 
     * @param reputationId
     *     The reputation_id
     */
    public void setReputationId(String reputationId) {
        this.reputationId = reputationId;
    }

    /**
     * 
     * @return
     *     The reviewLikeDislike
     */
    public ReviewLikeDislike getReviewLikeDislike() {
        return reviewLikeDislike;
    }

    /**
     * 
     * @param reviewLikeDislike
     *     The review_like_dislike
     */
    public void setReviewLikeDislike(ReviewLikeDislike reviewLikeDislike) {
        this.reviewLikeDislike = reviewLikeDislike;
    }

    /**
     * 
     * @return
     *     The rateSpeed
     */
    public String getRateSpeed() {
        return rateSpeed;
    }

    /**
     * 
     * @param rateSpeed
     *     The rate_speed
     */
    public void setRateSpeed(String rateSpeed) {
        this.rateSpeed = rateSpeed;
    }

    /**
     * 
     * @return
     *     The rateAccuractDesc
     */
    public String getRateAccuractDesc() {
        return rateAccuractDesc;
    }

    /**
     * 
     * @param rateAccuractDesc
     *     The rate_accuract_desc
     */
    public void setRateAccuractDesc(String rateAccuractDesc) {
        this.rateAccuractDesc = rateAccuractDesc;
    }

    /**
     * 
     * @return
     *     The feedbackTime
     */
    public String getFeedbackTime() {
        return feedbackTime;
    }

    /**
     * 
     * @param feedbackTime
     *     The feedback_time
     */
    public void setFeedbackTime(String feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    /**
     * 
     * @return
     *     The rateService
     */
    public String getRateService() {
        return rateService;
    }

    /**
     * 
     * @param rateService
     *     The rate_service
     */
    public void setRateService(String rateService) {
        this.rateService = rateService;
    }

    /**
     * 
     * @return
     *     The rateServiceDesc
     */
    public String getRateServiceDesc() {
        return rateServiceDesc;
    }

    /**
     * 
     * @param rateServiceDesc
     *     The rate_service_desc
     */
    public void setRateServiceDesc(String rateServiceDesc) {
        this.rateServiceDesc = rateServiceDesc;
    }

    /**
     * 
     * @return
     *     The rateSpeedDesc
     */
    public String getRateSpeedDesc() {
        return rateSpeedDesc;
    }

    /**
     * 
     * @param rateSpeedDesc
     *     The rate_speed_desc
     */
    public void setRateSpeedDesc(String rateSpeedDesc) {
        this.rateSpeedDesc = rateSpeedDesc;
    }

    /**
     * 
     * @return
     *     The owner
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * 
     * @param owner
     *     The owner
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * 
     * @return
     *     The feedbackUpdateTime
     */
    public String getFeedbackUpdateTime() {
        return feedbackUpdateTime;
    }

    /**
     * 
     * @param feedbackUpdateTime
     *     The feedback_update_time
     */
    public void setFeedbackUpdateTime(String feedbackUpdateTime) {
        this.feedbackUpdateTime = feedbackUpdateTime;
    }

    /**
     * 
     * @return
     *     The feedbackTimeFmt
     */
    public String getFeedbackTimeFmt() {
        return feedbackTimeFmt;
    }

    /**
     * 
     * @param feedbackTimeFmt
     *     The feedback_time_fmt
     */
    public void setFeedbackTimeFmt(String feedbackTimeFmt) {
        this.feedbackTimeFmt = feedbackTimeFmt;
    }

    /**
     * 
     * @return
     *     The reviewContent
     */
    public String getReviewContent() {
        return reviewContent;
    }

    /**
     * 
     * @param reviewContent
     *     The review_content
     */
    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    /**
     * 
     * @return
     *     The feedbackId
     */
    public String getFeedbackId() {
        return feedbackId;
    }

    /**
     * 
     * @param feedbackId
     *     The feedback_id
     */
    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    /**
     * 
     * @return
     *     The shopId
     */
    public String getShopId() {
        return shopId;
    }

    /**
     * 
     * @param shopId
     *     The shop_id
     */
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rateProductDesc);
        dest.writeInt(rateAccuracy);
        dest.writeString(productId);
        dest.writeInt(rateProduct);
        dest.writeString(reputationId);
        dest.writeString(rateSpeed);
        dest.writeString(rateAccuractDesc);
        dest.writeString(feedbackTime);
        dest.writeString(rateService);
        dest.writeString(rateServiceDesc);
        dest.writeString(rateSpeedDesc);
        dest.writeString(feedbackUpdateTime);
        dest.writeString(feedbackTimeFmt);
        dest.writeString(reviewContent);
        dest.writeString(feedbackId);
        dest.writeString(shopId);
        dest.writeValue(shopData);
        dest.writeValue(response);
        dest.writeValue(reviewerData);
        dest.writeValue(reviewLikeDislike);
    }
}
