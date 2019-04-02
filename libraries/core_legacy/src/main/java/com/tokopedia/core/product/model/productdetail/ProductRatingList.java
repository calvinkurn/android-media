package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
@Deprecated
public class ProductRatingList implements Parcelable {
    private static final String TAG = ProductRatingList.class.getSimpleName();

    @SerializedName("rating_rating_star_point")
    @Expose
    private Integer ratingRatingStarPoint;
    @SerializedName("rating_total_rate_accuracy_persen")
    @Expose
    private String ratingTotalRateAccuracyPersen;
    @SerializedName("rating_rate_service")
    @Expose
    private Integer ratingRateService;
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
    private Integer ratingRating;
    @SerializedName("rating_url_filter_rating")
    @Expose
    private String ratingUrlFilterRating;
    @SerializedName("rating_rate_speed")
    @Expose
    private Integer ratingRateSpeed;
    @SerializedName("rating_rate_accuracy")
    @Expose
    private Integer ratingRateAccuracy;
    @SerializedName("rating_rate_accuracy_fmt")
    @Expose
    private String ratingRateAccuracyFmt;
    @SerializedName("rating_rating_point")
    @Expose
    private Integer ratingRatingPoint;

    public ProductRatingList() {
    }

    public Integer getRatingRatingStarPoint() {
        return ratingRatingStarPoint;
    }

    public void setRatingRatingStarPoint(Integer ratingRatingStarPoint) {
        this.ratingRatingStarPoint = ratingRatingStarPoint;
    }

    public String getRatingTotalRateAccuracyPersen() {
        return ratingTotalRateAccuracyPersen;
    }

    public void setRatingTotalRateAccuracyPersen(String ratingTotalRateAccuracyPersen) {
        this.ratingTotalRateAccuracyPersen = ratingTotalRateAccuracyPersen;
    }

    public Integer getRatingRateService() {
        return ratingRateService;
    }

    public void setRatingRateService(Integer ratingRateService) {
        this.ratingRateService = ratingRateService;
    }

    public String getRatingRatingStarDesc() {
        return ratingRatingStarDesc;
    }

    public void setRatingRatingStarDesc(String ratingRatingStarDesc) {
        this.ratingRatingStarDesc = ratingRatingStarDesc;
    }

    public String getRatingRatingFmt() {
        return ratingRatingFmt;
    }

    public void setRatingRatingFmt(String ratingRatingFmt) {
        this.ratingRatingFmt = ratingRatingFmt;
    }

    public String getRatingTotalRatingPersen() {
        return ratingTotalRatingPersen;
    }

    public void setRatingTotalRatingPersen(String ratingTotalRatingPersen) {
        this.ratingTotalRatingPersen = ratingTotalRatingPersen;
    }

    public String getRatingUrlFilterRateAccuracy() {
        return ratingUrlFilterRateAccuracy;
    }

    public void setRatingUrlFilterRateAccuracy(String ratingUrlFilterRateAccuracy) {
        this.ratingUrlFilterRateAccuracy = ratingUrlFilterRateAccuracy;
    }

    public Integer getRatingRating() {
        return ratingRating;
    }

    public void setRatingRating(Integer ratingRating) {
        this.ratingRating = ratingRating;
    }

    public String getRatingUrlFilterRating() {
        return ratingUrlFilterRating;
    }

    public void setRatingUrlFilterRating(String ratingUrlFilterRating) {
        this.ratingUrlFilterRating = ratingUrlFilterRating;
    }

    public Integer getRatingRateSpeed() {
        return ratingRateSpeed;
    }

    public void setRatingRateSpeed(Integer ratingRateSpeed) {
        this.ratingRateSpeed = ratingRateSpeed;
    }

    public Integer getRatingRateAccuracy() {
        return ratingRateAccuracy;
    }

    public void setRatingRateAccuracy(Integer ratingRateAccuracy) {
        this.ratingRateAccuracy = ratingRateAccuracy;
    }

    public String getRatingRateAccuracyFmt() {
        return ratingRateAccuracyFmt;
    }

    public void setRatingRateAccuracyFmt(String ratingRateAccuracyFmt) {
        this.ratingRateAccuracyFmt = ratingRateAccuracyFmt;
    }

    public Integer getRatingRatingPoint() {
        return ratingRatingPoint;
    }

    public void setRatingRatingPoint(Integer ratingRatingPoint) {
        this.ratingRatingPoint = ratingRatingPoint;
    }

    protected ProductRatingList(Parcel in) {
        ratingRatingStarPoint = in.readByte() == 0x00 ? null : in.readInt();
        ratingTotalRateAccuracyPersen = in.readString();
        ratingRateService = in.readByte() == 0x00 ? null : in.readInt();
        ratingRatingStarDesc = in.readString();
        ratingRatingFmt = in.readString();
        ratingTotalRatingPersen = in.readString();
        ratingUrlFilterRateAccuracy = in.readString();
        ratingRating = in.readByte() == 0x00 ? null : in.readInt();
        ratingUrlFilterRating = in.readString();
        ratingRateSpeed = in.readByte() == 0x00 ? null : in.readInt();
        ratingRateAccuracy = in.readByte() == 0x00 ? null : in.readInt();
        ratingRateAccuracyFmt = in.readString();
        ratingRatingPoint = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (ratingRatingStarPoint == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(ratingRatingStarPoint);
        }
        dest.writeString(ratingTotalRateAccuracyPersen);
        if (ratingRateService == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(ratingRateService);
        }
        dest.writeString(ratingRatingStarDesc);
        dest.writeString(ratingRatingFmt);
        dest.writeString(ratingTotalRatingPersen);
        dest.writeString(ratingUrlFilterRateAccuracy);
        if (ratingRating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(ratingRating);
        }
        dest.writeString(ratingUrlFilterRating);
        if (ratingRateSpeed == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(ratingRateSpeed);
        }
        if (ratingRateAccuracy == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(ratingRateAccuracy);
        }
        dest.writeString(ratingRateAccuracyFmt);
        if (ratingRatingPoint == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(ratingRatingPoint);
        }
    }

    @SuppressWarnings("unused")
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


    public static class Builder {
        private Integer ratingRatingStarPoint;
        private String ratingTotalRateAccuracyPersen;
        private Integer ratingRateService;
        private String ratingRatingStarDesc;
        private String ratingRatingFmt;
        private String ratingTotalRatingPersen;
        private String ratingUrlFilterRateAccuracy;
        private Integer ratingRating;
        private String ratingUrlFilterRating;
        private Integer ratingRateSpeed;
        private Integer ratingRateAccuracy;
        private String ratingRateAccuracyFmt;
        private Integer ratingRatingPoint;

        private Builder() {
        }

        public static Builder aProductRatingList() {
            return new Builder();
        }

        public Builder setRatingRatingStarPoint(Integer ratingRatingStarPoint) {
            this.ratingRatingStarPoint = ratingRatingStarPoint;
            return this;
        }

        public Builder setRatingTotalRateAccuracyPersen(String ratingTotalRateAccuracyPersen) {
            this.ratingTotalRateAccuracyPersen = ratingTotalRateAccuracyPersen;
            return this;
        }

        public Builder setRatingRateService(Integer ratingRateService) {
            this.ratingRateService = ratingRateService;
            return this;
        }

        public Builder setRatingRatingStarDesc(String ratingRatingStarDesc) {
            this.ratingRatingStarDesc = ratingRatingStarDesc;
            return this;
        }

        public Builder setRatingRatingFmt(String ratingRatingFmt) {
            this.ratingRatingFmt = ratingRatingFmt;
            return this;
        }

        public Builder setRatingTotalRatingPersen(String ratingTotalRatingPersen) {
            this.ratingTotalRatingPersen = ratingTotalRatingPersen;
            return this;
        }

        public Builder setRatingUrlFilterRateAccuracy(String ratingUrlFilterRateAccuracy) {
            this.ratingUrlFilterRateAccuracy = ratingUrlFilterRateAccuracy;
            return this;
        }

        public Builder setRatingRating(Integer ratingRating) {
            this.ratingRating = ratingRating;
            return this;
        }

        public Builder setRatingUrlFilterRating(String ratingUrlFilterRating) {
            this.ratingUrlFilterRating = ratingUrlFilterRating;
            return this;
        }

        public Builder setRatingRateSpeed(Integer ratingRateSpeed) {
            this.ratingRateSpeed = ratingRateSpeed;
            return this;
        }

        public Builder setRatingRateAccuracy(Integer ratingRateAccuracy) {
            this.ratingRateAccuracy = ratingRateAccuracy;
            return this;
        }

        public Builder setRatingRateAccuracyFmt(String ratingRateAccuracyFmt) {
            this.ratingRateAccuracyFmt = ratingRateAccuracyFmt;
            return this;
        }

        public Builder setRatingRatingPoint(Integer ratingRatingPoint) {
            this.ratingRatingPoint = ratingRatingPoint;
            return this;
        }

        public Builder but() {
            return aProductRatingList().setRatingRatingStarPoint(ratingRatingStarPoint).setRatingTotalRateAccuracyPersen(ratingTotalRateAccuracyPersen).setRatingRateService(ratingRateService).setRatingRatingStarDesc(ratingRatingStarDesc).setRatingRatingFmt(ratingRatingFmt).setRatingTotalRatingPersen(ratingTotalRatingPersen).setRatingUrlFilterRateAccuracy(ratingUrlFilterRateAccuracy).setRatingRating(ratingRating).setRatingUrlFilterRating(ratingUrlFilterRating).setRatingRateSpeed(ratingRateSpeed).setRatingRateAccuracy(ratingRateAccuracy).setRatingRateAccuracyFmt(ratingRateAccuracyFmt).setRatingRatingPoint(ratingRatingPoint);
        }

        public ProductRatingList build() {
            ProductRatingList productRatingList = new ProductRatingList();
            productRatingList.setRatingRatingStarPoint(ratingRatingStarPoint);
            productRatingList.setRatingTotalRateAccuracyPersen(ratingTotalRateAccuracyPersen);
            productRatingList.setRatingRateService(ratingRateService);
            productRatingList.setRatingRatingStarDesc(ratingRatingStarDesc);
            productRatingList.setRatingRatingFmt(ratingRatingFmt);
            productRatingList.setRatingTotalRatingPersen(ratingTotalRatingPersen);
            productRatingList.setRatingUrlFilterRateAccuracy(ratingUrlFilterRateAccuracy);
            productRatingList.setRatingRating(ratingRating);
            productRatingList.setRatingUrlFilterRating(ratingUrlFilterRating);
            productRatingList.setRatingRateSpeed(ratingRateSpeed);
            productRatingList.setRatingRateAccuracy(ratingRateAccuracy);
            productRatingList.setRatingRateAccuracyFmt(ratingRateAccuracyFmt);
            productRatingList.setRatingRatingPoint(ratingRatingPoint);
            return productRatingList;
        }
    }
}
