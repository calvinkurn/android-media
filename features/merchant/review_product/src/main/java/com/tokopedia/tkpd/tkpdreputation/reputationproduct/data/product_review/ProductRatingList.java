
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductRatingList implements Parcelable{

    @SerializedName("rating_rating_star_point")
    @Expose
    private int ratingRatingStarPoint;
    @SerializedName("rating_total_rate_accuracy_persen")
    @Expose
    private String ratingTotalRateAccuracyPersen;
    @SerializedName("rating_rate_service")
    @Expose
    private int ratingRateService;
    @SerializedName("rating_rating_star_desc")
    @Expose
    private String ratingRatingStarDesc;
    @SerializedName("rating_rating_fmt")
    @Expose
    private String ratingRatingFmt;
    @SerializedName("rating_total_rating_persen")
    @Expose
    private String ratingTotalRatingPersen;
    @SerializedName("rating_url_filter_rate_accuracy")
    @Expose
    private String ratingUrlFilterRateAccuracy;
    @SerializedName("rating_rating")
    @Expose
    private int ratingRating;
    @SerializedName("rating_url_filter_rating")
    @Expose
    private String ratingUrlFilterRating;
    @SerializedName("rating_rate_speed")
    @Expose
    private int ratingRateSpeed;
    @SerializedName("rating_rate_accuracy")
    @Expose
    private int ratingRateAccuracy;
    @SerializedName("rating_rate_accuracy_fmt")
    @Expose
    private String ratingRateAccuracyFmt;
    @SerializedName("rating_rating_point")
    @Expose
    private int ratingRatingPoint;

    protected ProductRatingList(Parcel in) {
        ratingRatingStarPoint = in.readInt();
        ratingTotalRateAccuracyPersen = in.readString();
        ratingRateService = in.readInt();
        ratingRatingStarDesc = in.readString();
        ratingRatingFmt = in.readString();
        ratingTotalRatingPersen = in.readString();
        ratingUrlFilterRateAccuracy = in.readString();
        ratingRating = in.readInt();
        ratingUrlFilterRating = in.readString();
        ratingRateSpeed = in.readInt();
        ratingRateAccuracy = in.readInt();
        ratingRateAccuracyFmt = in.readString();
        ratingRatingPoint = in.readInt();
    }

    public static final Creator<ProductRatingList> CREATOR = new Creator<ProductRatingList>() {
        @Override
        public ProductRatingList createFromParcel(Parcel in) {
            return new ProductRatingList(in);
        }

        @Override
        public ProductRatingList[] newArray(int size) {
            return new ProductRatingList[size];
        }
    };

    /**
     * 
     * @return
     *     The ratingRatingStarPoint
     */
    public int getRatingRatingStarPoint() {
        return ratingRatingStarPoint;
    }

    /**
     * 
     * @param ratingRatingStarPoint
     *     The rating_rating_star_point
     */
    public void setRatingRatingStarPoint(int ratingRatingStarPoint) {
        this.ratingRatingStarPoint = ratingRatingStarPoint;
    }

    /**
     * 
     * @return
     *     The ratingTotalRateAccuracyPersen
     */
    public String getRatingTotalRateAccuracyPersen() {
        return ratingTotalRateAccuracyPersen;
    }

    /**
     * 
     * @param ratingTotalRateAccuracyPersen
     *     The rating_total_rate_accuracy_persen
     */
    public void setRatingTotalRateAccuracyPersen(String ratingTotalRateAccuracyPersen) {
        this.ratingTotalRateAccuracyPersen = ratingTotalRateAccuracyPersen;
    }

    /**
     * 
     * @return
     *     The ratingRateService
     */
    public int getRatingRateService() {
        return ratingRateService;
    }

    /**
     * 
     * @param ratingRateService
     *     The rating_rate_service
     */
    public void setRatingRateService(int ratingRateService) {
        this.ratingRateService = ratingRateService;
    }

    /**
     * 
     * @return
     *     The ratingRatingStarDesc
     */
    public String getRatingRatingStarDesc() {
        return ratingRatingStarDesc;
    }

    /**
     * 
     * @param ratingRatingStarDesc
     *     The rating_rating_star_desc
     */
    public void setRatingRatingStarDesc(String ratingRatingStarDesc) {
        this.ratingRatingStarDesc = ratingRatingStarDesc;
    }

    /**
     * 
     * @return
     *     The ratingRatingFmt
     */
    public String getRatingRatingFmt() {
        return ratingRatingFmt;
    }

    /**
     * 
     * @param ratingRatingFmt
     *     The rating_rating_fmt
     */
    public void setRatingRatingFmt(String ratingRatingFmt) {
        this.ratingRatingFmt = ratingRatingFmt;
    }

    /**
     * 
     * @return
     *     The ratingTotalRatingPersen
     */
    public String getRatingTotalRatingPersen() {
        return ratingTotalRatingPersen;
    }

    /**
     * 
     * @param ratingTotalRatingPersen
     *     The rating_total_rating_persen
     */
    public void setRatingTotalRatingPersen(String ratingTotalRatingPersen) {
        this.ratingTotalRatingPersen = ratingTotalRatingPersen;
    }

    /**
     * 
     * @return
     *     The ratingUrlFilterRateAccuracy
     */
    public String getRatingUrlFilterRateAccuracy() {
        return ratingUrlFilterRateAccuracy;
    }

    /**
     * 
     * @param ratingUrlFilterRateAccuracy
     *     The rating_url_filter_rate_accuracy
     */
    public void setRatingUrlFilterRateAccuracy(String ratingUrlFilterRateAccuracy) {
        this.ratingUrlFilterRateAccuracy = ratingUrlFilterRateAccuracy;
    }

    /**
     * 
     * @return
     *     The ratingRating
     */
    public int getRatingRating() {
        return ratingRating;
    }

    /**
     * 
     * @param ratingRating
     *     The rating_rating
     */
    public void setRatingRating(int ratingRating) {
        this.ratingRating = ratingRating;
    }

    /**
     * 
     * @return
     *     The ratingUrlFilterRating
     */
    public String getRatingUrlFilterRating() {
        return ratingUrlFilterRating;
    }

    /**
     * 
     * @param ratingUrlFilterRating
     *     The rating_url_filter_rating
     */
    public void setRatingUrlFilterRating(String ratingUrlFilterRating) {
        this.ratingUrlFilterRating = ratingUrlFilterRating;
    }

    /**
     * 
     * @return
     *     The ratingRateSpeed
     */
    public int getRatingRateSpeed() {
        return ratingRateSpeed;
    }

    /**
     * 
     * @param ratingRateSpeed
     *     The rating_rate_speed
     */
    public void setRatingRateSpeed(int ratingRateSpeed) {
        this.ratingRateSpeed = ratingRateSpeed;
    }

    /**
     * 
     * @return
     *     The ratingRateAccuracy
     */
    public int getRatingRateAccuracy() {
        return ratingRateAccuracy;
    }

    /**
     * 
     * @param ratingRateAccuracy
     *     The rating_rate_accuracy
     */
    public void setRatingRateAccuracy(int ratingRateAccuracy) {
        this.ratingRateAccuracy = ratingRateAccuracy;
    }

    /**
     * 
     * @return
     *     The ratingRateAccuracyFmt
     */
    public String getRatingRateAccuracyFmt() {
        return ratingRateAccuracyFmt;
    }

    /**
     * 
     * @param ratingRateAccuracyFmt
     *     The rating_rate_accuracy_fmt
     */
    public void setRatingRateAccuracyFmt(String ratingRateAccuracyFmt) {
        this.ratingRateAccuracyFmt = ratingRateAccuracyFmt;
    }

    /**
     * 
     * @return
     *     The ratingRatingPoint
     */
    public int getRatingRatingPoint() {
        return ratingRatingPoint;
    }

    /**
     * 
     * @param ratingRatingPoint
     *     The rating_rating_point
     */
    public void setRatingRatingPoint(int ratingRatingPoint) {
        this.ratingRatingPoint = ratingRatingPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ratingRatingStarPoint);
        dest.writeString(ratingTotalRateAccuracyPersen);
        dest.writeInt(ratingRateService);
        dest.writeString(ratingRatingStarDesc);
        dest.writeString(ratingRatingFmt);
        dest.writeString(ratingTotalRatingPersen);
        dest.writeString(ratingUrlFilterRateAccuracy);
        dest.writeInt(ratingRating);
        dest.writeString(ratingUrlFilterRating);
        dest.writeInt(ratingRateSpeed);
        dest.writeInt(ratingRateAccuracy);
        dest.writeString(ratingRateAccuracyFmt);
        dest.writeInt(ratingRatingPoint);
    }


}
