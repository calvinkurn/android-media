package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
@Deprecated
public class ProductRating implements Parcelable{
    private static final String TAG = ProductRating.class.getSimpleName();

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
    private Integer productRatingStarPoint;
    @SerializedName("product_rating_star_desc")
    @Expose
    private String productRatingStarDesc;
    @SerializedName("product_negative_review_rating")
    @Expose
    private String productNegativeReviewRating;
    @SerializedName("product_review")
    @Expose
    private Integer productReview;
    @SerializedName("product_rate_accuracy")
    @Expose
    private Double productRateAccuracy;
    @SerializedName("product_accuracy_star_desc")
    @Expose
    private String productAccuracyStarDesc;
    @SerializedName("product_rating")
    @Expose
    private Double productRating;
    @SerializedName("product_netral_review_rate_accuracy")
    @Expose
    private String productNetralReviewRateAccuracy;
    @SerializedName("product_accuracy_star_rate")
    @Expose
    private Integer productAccuracyStarRate;
    @SerializedName("product_positive_review_rate_accuracy")
    @Expose
    private String productPositiveReviewRateAccuracy;
    @SerializedName("product_rating_list")
    @Expose
    private List<ProductRatingList> productRatingList = new ArrayList<ProductRatingList>();
    @SerializedName("product_negative_review_rate_accuracy")
    @Expose
    private String productNegativeReviewRateAccuracy;

    public ProductRating() {
    }

    public String getProductRatingPoint() {
        return productRatingPoint;
    }

    public void setProductRatingPoint(String productRatingPoint) {
        this.productRatingPoint = productRatingPoint;
    }

    public String getProductRateAccuracyPoint() {
        return productRateAccuracyPoint;
    }

    public void setProductRateAccuracyPoint(String productRateAccuracyPoint) {
        this.productRateAccuracyPoint = productRateAccuracyPoint;
    }

    public String getProductPositiveReviewRating() {
        return productPositiveReviewRating;
    }

    public void setProductPositiveReviewRating(String productPositiveReviewRating) {
        this.productPositiveReviewRating = productPositiveReviewRating;
    }

    public String getProductNetralReviewRating() {
        return productNetralReviewRating;
    }

    public void setProductNetralReviewRating(String productNetralReviewRating) {
        this.productNetralReviewRating = productNetralReviewRating;
    }

    public Integer getProductRatingStarPoint() {
        return productRatingStarPoint;
    }

    public void setProductRatingStarPoint(Integer productRatingStarPoint) {
        this.productRatingStarPoint = productRatingStarPoint;
    }

    public String getProductRatingStarDesc() {
        return productRatingStarDesc;
    }

    public void setProductRatingStarDesc(String productRatingStarDesc) {
        this.productRatingStarDesc = productRatingStarDesc;
    }

    public String getProductNegativeReviewRating() {
        return productNegativeReviewRating;
    }

    public void setProductNegativeReviewRating(String productNegativeReviewRating) {
        this.productNegativeReviewRating = productNegativeReviewRating;
    }

    public Integer getProductReview() {
        return productReview;
    }

    public void setProductReview(Integer productReview) {
        this.productReview = productReview;
    }

    public Double getProductRateAccuracy() {
        return productRateAccuracy;
    }

    public void setProductRateAccuracy(Double productRateAccuracy) {
        this.productRateAccuracy = productRateAccuracy;
    }

    public String getProductAccuracyStarDesc() {
        return productAccuracyStarDesc;
    }

    public void setProductAccuracyStarDesc(String productAccuracyStarDesc) {
        this.productAccuracyStarDesc = productAccuracyStarDesc;
    }

    public Double getProductRating() {
        return productRating;
    }

    public void setProductRating(Double productRating) {
        this.productRating = productRating;
    }

    public String getProductNetralReviewRateAccuracy() {
        return productNetralReviewRateAccuracy;
    }

    public void setProductNetralReviewRateAccuracy(String productNetralReviewRateAccuracy) {
        this.productNetralReviewRateAccuracy = productNetralReviewRateAccuracy;
    }

    public Integer getProductAccuracyStarRate() {
        return productAccuracyStarRate;
    }

    public void setProductAccuracyStarRate(Integer productAccuracyStarRate) {
        this.productAccuracyStarRate = productAccuracyStarRate;
    }

    public String getProductPositiveReviewRateAccuracy() {
        return productPositiveReviewRateAccuracy;
    }

    public void setProductPositiveReviewRateAccuracy(String productPositiveReviewRateAccuracy) {
        this.productPositiveReviewRateAccuracy = productPositiveReviewRateAccuracy;
    }

    public List<ProductRatingList> getProductRatingList() {
        return productRatingList;
    }

    public void setProductRatingList(List<ProductRatingList> productRatingList) {
        this.productRatingList = productRatingList;
    }

    public String getProductNegativeReviewRateAccuracy() {
        return productNegativeReviewRateAccuracy;
    }

    public void setProductNegativeReviewRateAccuracy(String productNegativeReviewRateAccuracy) {
        this.productNegativeReviewRateAccuracy = productNegativeReviewRateAccuracy;
    }

    protected ProductRating(Parcel in) {
        productRatingPoint = in.readString();
        productRateAccuracyPoint = in.readString();
        productPositiveReviewRating = in.readString();
        productNetralReviewRating = in.readString();
        productRatingStarPoint = in.readByte() == 0x00 ? null : in.readInt();
        productRatingStarDesc = in.readString();
        productNegativeReviewRating = in.readString();
        productReview = in.readByte() == 0x00 ? null : in.readInt();
        productRateAccuracy = in.readByte() == 0x00 ? null : in.readDouble();
        productAccuracyStarDesc = in.readString();
        productRating = in.readByte() == 0x00 ? null : in.readDouble();
        productNetralReviewRateAccuracy = in.readString();
        productAccuracyStarRate = in.readByte() == 0x00 ? null : in.readInt();
        productPositiveReviewRateAccuracy = in.readString();
        if (in.readByte() == 0x01) {
            productRatingList = new ArrayList<ProductRatingList>();
            in.readList(productRatingList, ProductRatingList.class.getClassLoader());
        } else {
            productRatingList = null;
        }
        productNegativeReviewRateAccuracy = in.readString();
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
        if (productRatingStarPoint == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productRatingStarPoint);
        }
        dest.writeString(productRatingStarDesc);
        dest.writeString(productNegativeReviewRating);
        if (productReview == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productReview);
        }
        if (productRateAccuracy == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(productRateAccuracy);
        }
        dest.writeString(productAccuracyStarDesc);
        if (productRating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(productRating);
        }
        dest.writeString(productNetralReviewRateAccuracy);
        if (productAccuracyStarRate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productAccuracyStarRate);
        }
        dest.writeString(productPositiveReviewRateAccuracy);
        if (productRatingList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(productRatingList);
        }
        dest.writeString(productNegativeReviewRateAccuracy);
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductRating> CREATOR = new Creator<ProductRating>() {
        @Override
        public ProductRating createFromParcel(Parcel in) {
            return new ProductRating(in);
        }

        @Override
        public ProductRating[] newArray(int size) {
            return new ProductRating[size];
        }
    };


    public static class Builder {
        private String productRatingPoint;
        private String productRateAccuracyPoint;
        private String productPositiveReviewRating;
        private String productNetralReviewRating;
        private Integer productRatingStarPoint;
        private String productRatingStarDesc;
        private String productNegativeReviewRating;
        private Integer productReview;
        private Double productRateAccuracy;
        private String productAccuracyStarDesc;
        private Double productRating;
        private String productNetralReviewRateAccuracy;
        private Integer productAccuracyStarRate;
        private String productPositiveReviewRateAccuracy;
        private List<ProductRatingList> productRatingList = new ArrayList<ProductRatingList>();
        private String productNegativeReviewRateAccuracy;

        private Builder() {
        }

        public static Builder aProductRating() {
            return new Builder();
        }

        public Builder setProductRatingPoint(String productRatingPoint) {
            this.productRatingPoint = productRatingPoint;
            return this;
        }

        public Builder setProductRateAccuracyPoint(String productRateAccuracyPoint) {
            this.productRateAccuracyPoint = productRateAccuracyPoint;
            return this;
        }

        public Builder setProductPositiveReviewRating(String productPositiveReviewRating) {
            this.productPositiveReviewRating = productPositiveReviewRating;
            return this;
        }

        public Builder setProductNetralReviewRating(String productNetralReviewRating) {
            this.productNetralReviewRating = productNetralReviewRating;
            return this;
        }

        public Builder setProductRatingStarPoint(Integer productRatingStarPoint) {
            this.productRatingStarPoint = productRatingStarPoint;
            return this;
        }

        public Builder setProductRatingStarDesc(String productRatingStarDesc) {
            this.productRatingStarDesc = productRatingStarDesc;
            return this;
        }

        public Builder setProductNegativeReviewRating(String productNegativeReviewRating) {
            this.productNegativeReviewRating = productNegativeReviewRating;
            return this;
        }

        public Builder setProductReview(Integer productReview) {
            this.productReview = productReview;
            return this;
        }

        public Builder setProductRateAccuracy(Double productRateAccuracy) {
            this.productRateAccuracy = productRateAccuracy;
            return this;
        }

        public Builder setProductAccuracyStarDesc(String productAccuracyStarDesc) {
            this.productAccuracyStarDesc = productAccuracyStarDesc;
            return this;
        }

        public Builder setProductRating(Double productRating) {
            this.productRating = productRating;
            return this;
        }

        public Builder setProductNetralReviewRateAccuracy(String productNetralReviewRateAccuracy) {
            this.productNetralReviewRateAccuracy = productNetralReviewRateAccuracy;
            return this;
        }

        public Builder setProductAccuracyStarRate(Integer productAccuracyStarRate) {
            this.productAccuracyStarRate = productAccuracyStarRate;
            return this;
        }

        public Builder setProductPositiveReviewRateAccuracy(String productPositiveReviewRateAccuracy) {
            this.productPositiveReviewRateAccuracy = productPositiveReviewRateAccuracy;
            return this;
        }

        public Builder setProductRatingList(List<ProductRatingList> productRatingList) {
            this.productRatingList = productRatingList;
            return this;
        }

        public Builder setProductNegativeReviewRateAccuracy(String productNegativeReviewRateAccuracy) {
            this.productNegativeReviewRateAccuracy = productNegativeReviewRateAccuracy;
            return this;
        }

        public Builder but() {
            return aProductRating().setProductRatingPoint(productRatingPoint).setProductRateAccuracyPoint(productRateAccuracyPoint).setProductPositiveReviewRating(productPositiveReviewRating).setProductNetralReviewRating(productNetralReviewRating).setProductRatingStarPoint(productRatingStarPoint).setProductRatingStarDesc(productRatingStarDesc).setProductNegativeReviewRating(productNegativeReviewRating).setProductReview(productReview).setProductRateAccuracy(productRateAccuracy).setProductAccuracyStarDesc(productAccuracyStarDesc).setProductRating(productRating).setProductNetralReviewRateAccuracy(productNetralReviewRateAccuracy).setProductAccuracyStarRate(productAccuracyStarRate).setProductPositiveReviewRateAccuracy(productPositiveReviewRateAccuracy).setProductRatingList(productRatingList).setProductNegativeReviewRateAccuracy(productNegativeReviewRateAccuracy);
        }

        public ProductRating build() {
            ProductRating productRating = new ProductRating();
            productRating.setProductRatingPoint(productRatingPoint);
            productRating.setProductRateAccuracyPoint(productRateAccuracyPoint);
            productRating.setProductPositiveReviewRating(productPositiveReviewRating);
            productRating.setProductNetralReviewRating(productNetralReviewRating);
            productRating.setProductRatingStarPoint(productRatingStarPoint);
            productRating.setProductRatingStarDesc(productRatingStarDesc);
            productRating.setProductNegativeReviewRating(productNegativeReviewRating);
            productRating.setProductReview(productReview);
            productRating.setProductRateAccuracy(productRateAccuracy);
            productRating.setProductAccuracyStarDesc(productAccuracyStarDesc);
            productRating.setProductRating(this.productRating);
            productRating.setProductNetralReviewRateAccuracy(productNetralReviewRateAccuracy);
            productRating.setProductAccuracyStarRate(productAccuracyStarRate);
            productRating.setProductPositiveReviewRateAccuracy(productPositiveReviewRateAccuracy);
            productRating.setProductRatingList(productRatingList);
            productRating.setProductNegativeReviewRateAccuracy(productNegativeReviewRateAccuracy);
            return productRating;
        }
    }
}
