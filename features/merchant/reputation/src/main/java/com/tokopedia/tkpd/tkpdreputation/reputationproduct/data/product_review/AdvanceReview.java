
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;


public class AdvanceReview implements Parcelable{

    @SerializedName("product_rating_point")
    @Expose
    private String productRatingPoint;
    @SerializedName("product_rate_accuracy_point")
    @Expose
    private String productRateAccuracyPoint;
    @SerializedName("product_positive_review_rating")
    @Expose
    private String productPositiveReviewRating;
    @SerializedName("product_netral_review_rating")
    @Expose
    private String productNetralReviewRating;
    @SerializedName("product_rating_star_point")
    @Expose
    private int productRatingStarPoint;
    @SerializedName("product_rating_star_desc")
    @Expose
    private String productRatingStarDesc;
    @SerializedName("product_negative_review_rating")
    @Expose
    private String productNegativeReviewRating;
    @SerializedName("product_review")
    @Expose
    private String productReview;
    @SerializedName("product_rate_accuracy")
    @Expose
    private double productRateAccuracy;
    @SerializedName("product_accuracy_star_desc")
    @Expose
    private String productAccuracyStarDesc;
    @SerializedName("product_rating")
    @Expose
    private double productRating;
    @SerializedName("product_netral_review_rate_accuracy")
    @Expose
    private String productNetralReviewRateAccuracy;
    @SerializedName("product_accuracy_star_rate")
    @Expose
    private int productAccuracyStarRate;
    @SerializedName("product_positive_review_rate_accuracy")
    @Expose
    private String productPositiveReviewRateAccuracy;
    @SerializedName("product_rating_list")
    @Expose
    private List<ProductRatingList> productRatingList = new ArrayList<ProductRatingList>();
    @SerializedName("product_negative_review_rate_accuracy")
    @Expose
    private String productNegativeReviewRateAccuracy;

    protected AdvanceReview(Parcel in) {
        productRatingPoint = in.readString();
        productRateAccuracyPoint = in.readString();
        productPositiveReviewRating = in.readString();
        productNetralReviewRating = in.readString();
        productRatingStarPoint = in.readInt();
        productRatingStarDesc = in.readString();
        productNegativeReviewRating = in.readString();
        productReview = in.readString();
        productRateAccuracy = in.readDouble();
        productAccuracyStarDesc = in.readString();
        productRating = in.readDouble();
        productNetralReviewRateAccuracy = in.readString();
        productAccuracyStarRate = in.readInt();
        productPositiveReviewRateAccuracy = in.readString();
        productNegativeReviewRateAccuracy = in.readString();
        productRatingList = new ArrayList<>();
        in.readTypedList(productRatingList,ProductRatingList.CREATOR);
    }

    public static final Creator<AdvanceReview> CREATOR = new Creator<AdvanceReview>() {
        @Override
        public AdvanceReview createFromParcel(Parcel in) {
            return new AdvanceReview(in);
        }

        @Override
        public AdvanceReview[] newArray(int size) {
            return new AdvanceReview[size];
        }
    };

    /**
     * 
     * @return
     *     The productRatingPoint
     */
    public String getProductRatingPoint() {
        return productRatingPoint;
    }

    /**
     * 
     * @param productRatingPoint
     *     The product_rating_point
     */
    public void setProductRatingPoint(String productRatingPoint) {
        this.productRatingPoint = productRatingPoint;
    }

    /**
     * 
     * @return
     *     The productRateAccuracyPoint
     */
    public String getProductRateAccuracyPoint() {
        return productRateAccuracyPoint;
    }

    /**
     * 
     * @param productRateAccuracyPoint
     *     The product_rate_accuracy_point
     */
    public void setProductRateAccuracyPoint(String productRateAccuracyPoint) {
        this.productRateAccuracyPoint = productRateAccuracyPoint;
    }

    /**
     * 
     * @return
     *     The productPositiveReviewRating
     */
    public String getProductPositiveReviewRating() {
        return productPositiveReviewRating;
    }

    /**
     * 
     * @param productPositiveReviewRating
     *     The product_positive_review_rating
     */
    public void setProductPositiveReviewRating(String productPositiveReviewRating) {
        this.productPositiveReviewRating = productPositiveReviewRating;
    }

    /**
     * 
     * @return
     *     The productNetralReviewRating
     */
    public String getProductNetralReviewRating() {
        return productNetralReviewRating;
    }

    /**
     * 
     * @param productNetralReviewRating
     *     The product_netral_review_rating
     */
    public void setProductNetralReviewRating(String productNetralReviewRating) {
        this.productNetralReviewRating = productNetralReviewRating;
    }

    /**
     * 
     * @return
     *     The productRatingStarPoint
     */
    public int getProductRatingStarPoint() {
        return productRatingStarPoint;
    }

    /**
     * 
     * @param productRatingStarPoint
     *     The product_rating_star_point
     */
    public void setProductRatingStarPoint(int productRatingStarPoint) {
        this.productRatingStarPoint = productRatingStarPoint;
    }

    /**
     * 
     * @return
     *     The productRatingStarDesc
     */
    public String getProductRatingStarDesc() {
        return productRatingStarDesc;
    }

    /**
     * 
     * @param productRatingStarDesc
     *     The product_rating_star_desc
     */
    public void setProductRatingStarDesc(String productRatingStarDesc) {
        this.productRatingStarDesc = productRatingStarDesc;
    }

    /**
     * 
     * @return
     *     The productNegativeReviewRating
     */
    public String getProductNegativeReviewRating() {
        return productNegativeReviewRating;
    }

    /**
     * 
     * @param productNegativeReviewRating
     *     The product_negative_review_rating
     */
    public void setProductNegativeReviewRating(String productNegativeReviewRating) {
        this.productNegativeReviewRating = productNegativeReviewRating;
    }

    /**
     * 
     * @return
     *     The productReview
     */
    public String getProductReview() {
        return MethodChecker.fromHtml(productReview).toString();
    }

    /**
     * 
     * @param productReview
     *     The product_review
     */
    public void setProductReview(String productReview) {
        this.productReview = productReview;
    }

    /**
     * 
     * @return
     *     The productRateAccuracy
     */
    public double getProductRateAccuracy() {
        return productRateAccuracy;
    }

    /**
     * 
     * @param productRateAccuracy
     *     The product_rate_accuracy
     */
    public void setProductRateAccuracy(double productRateAccuracy) {
        this.productRateAccuracy = productRateAccuracy;
    }

    /**
     * 
     * @return
     *     The productAccuracyStarDesc
     */
    public String getProductAccuracyStarDesc() {
        return productAccuracyStarDesc;
    }

    /**
     * 
     * @param productAccuracyStarDesc
     *     The product_accuracy_star_desc
     */
    public void setProductAccuracyStarDesc(String productAccuracyStarDesc) {
        this.productAccuracyStarDesc = productAccuracyStarDesc;
    }

    /**
     * 
     * @return
     *     The productRating
     */
    public double getProductRating() {
        return productRating;
    }

    /**
     * 
     * @param productRating
     *     The product_rating
     */
    public void setProductRating(double productRating) {
        this.productRating = productRating;
    }

    /**
     * 
     * @return
     *     The productNetralReviewRateAccuracy
     */
    public String getProductNetralReviewRateAccuracy() {
        return productNetralReviewRateAccuracy;
    }

    /**
     * 
     * @param productNetralReviewRateAccuracy
     *     The product_netral_review_rate_accuracy
     */
    public void setProductNetralReviewRateAccuracy(String productNetralReviewRateAccuracy) {
        this.productNetralReviewRateAccuracy = productNetralReviewRateAccuracy;
    }

    /**
     * 
     * @return
     *     The productAccuracyStarRate
     */
    public int getProductAccuracyStarRate() {
        return productAccuracyStarRate;
    }

    /**
     * 
     * @param productAccuracyStarRate
     *     The product_accuracy_star_rate
     */
    public void setProductAccuracyStarRate(int productAccuracyStarRate) {
        this.productAccuracyStarRate = productAccuracyStarRate;
    }

    /**
     * 
     * @return
     *     The productPositiveReviewRateAccuracy
     */
    public String getProductPositiveReviewRateAccuracy() {
        return productPositiveReviewRateAccuracy;
    }

    /**
     * 
     * @param productPositiveReviewRateAccuracy
     *     The product_positive_review_rate_accuracy
     */
    public void setProductPositiveReviewRateAccuracy(String productPositiveReviewRateAccuracy) {
        this.productPositiveReviewRateAccuracy = productPositiveReviewRateAccuracy;
    }

    /**
     * 
     * @return
     *     The productRatingList
     */
    public List<ProductRatingList> getProductRatingList() {
        return productRatingList;
    }

    /**
     * 
     * @param productRatingList
     *     The product_rating_list
     */
    public void setProductRatingList(List<ProductRatingList> productRatingList) {
        this.productRatingList = productRatingList;
    }

    /**
     * 
     * @return
     *     The productNegativeReviewRateAccuracy
     */
    public String getProductNegativeReviewRateAccuracy() {
        return productNegativeReviewRateAccuracy;
    }

    /**
     * 
     * @param productNegativeReviewRateAccuracy
     *     The product_negative_review_rate_accuracy
     */
    public void setProductNegativeReviewRateAccuracy(String productNegativeReviewRateAccuracy) {
        this.productNegativeReviewRateAccuracy = productNegativeReviewRateAccuracy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productRatingPoint);
        dest.writeString(productRateAccuracyPoint);
        dest.writeString(productPositiveReviewRating);
        dest.writeString(productNetralReviewRating);
        dest.writeInt(productRatingStarPoint);
        dest.writeString(productRatingStarDesc);
        dest.writeString(productNegativeReviewRating);
        dest.writeString(productReview);
        dest.writeDouble(productRateAccuracy);
        dest.writeString(productAccuracyStarDesc);
        dest.writeDouble(productRating);
        dest.writeString(productNetralReviewRateAccuracy);
        dest.writeInt(productAccuracyStarRate);
        dest.writeString(productPositiveReviewRateAccuracy);
        dest.writeString(productNegativeReviewRateAccuracy);
        dest.writeTypedList(productRatingList);
    }
}
