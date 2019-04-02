package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
@Deprecated
public class ProductStatistic implements Parcelable {
    private static final String TAG = ProductStatistic.class.getSimpleName();

    @SerializedName("product_sold_count")
    @Expose
    private String productSoldCount;
    @SerializedName("product_transaction_count")
    @Expose
    private String productTransactionCount;
    @SerializedName("product_rating_point")
    @Expose
    private String productRatingPoint;
    @SerializedName("product_cancel_rate")
    @Expose
    private String productCancelRate;
    @SerializedName("product_review_count")
    @Expose
    private String productReviewCount;
    @SerializedName("product_view_count")
    @Expose
    private String productViewCount;
    @SerializedName("product_success_rate")
    @Expose
    private String productSuccessRate;
    @SerializedName("product_rating_desc")
    @Expose
    private String productRatingDesc;
    @SerializedName("product_talk_count")
    @Expose
    private String productTalkCount;

    public ProductStatistic() {
    }

    public String getProductSoldCount() {
        return productSoldCount;
    }

    public void setProductSoldCount(String productSoldCount) {
        this.productSoldCount = productSoldCount;
    }

    public String getProductTransactionCount() {
        return productTransactionCount;
    }

    public void setProductTransactionCount(String productTransactionCount) {
        this.productTransactionCount = productTransactionCount;
    }

    public String getProductRatingPoint() {
        return productRatingPoint;
    }

    public void setProductRatingPoint(String productRatingPoint) {
        this.productRatingPoint = productRatingPoint;
    }

    public String getProductCancelRate() {
        return productCancelRate;
    }

    public void setProductCancelRate(String productCancelRate) {
        this.productCancelRate = productCancelRate;
    }

    public String getProductReviewCount() {
        return productReviewCount;
    }

    public void setProductReviewCount(String productReviewCount) {
        this.productReviewCount = productReviewCount;
    }

    public String getProductViewCount() {
        return productViewCount;
    }

    public void setProductViewCount(String productViewCount) {
        this.productViewCount = productViewCount;
    }

    public String getProductSuccessRate() {
        return productSuccessRate;
    }

    public void setProductSuccessRate(String productSuccessRate) {
        this.productSuccessRate = productSuccessRate;
    }

    public String getProductRatingDesc() {
        return productRatingDesc;
    }

    public void setProductRatingDesc(String productRatingDesc) {
        this.productRatingDesc = productRatingDesc;
    }

    public String getProductTalkCount() {
        return productTalkCount;
    }

    public void setProductTalkCount(String productTalkCount) {
        this.productTalkCount = productTalkCount;
    }

    protected ProductStatistic(Parcel in) {
        productSoldCount = in.readString();
        productTransactionCount = in.readString();
        productRatingPoint = in.readString();
        productCancelRate = in.readString();
        productReviewCount = in.readString();
        productViewCount = in.readString();
        productSuccessRate = in.readString();
        productRatingDesc = in.readString();
        productTalkCount = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productSoldCount);
        dest.writeString(productTransactionCount);
        dest.writeString(productRatingPoint);
        dest.writeString(productCancelRate);
        dest.writeString(productReviewCount);
        dest.writeString(productViewCount);
        dest.writeString(productSuccessRate);
        dest.writeString(productRatingDesc);
        dest.writeString(productTalkCount);
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductStatistic> CREATOR = new Creator<ProductStatistic>() {
        @Override
        public ProductStatistic createFromParcel(Parcel in) {
            return new ProductStatistic(in);
        }

        @Override
        public ProductStatistic[] newArray(int size) {
            return new ProductStatistic[size];
        }
    };


    public static class Builder {
        private String productSoldCount;
        private String productTransactionCount;
        private String productRatingPoint;
        private String productCancelRate;
        private String productReviewCount;
        private String productViewCount;
        private String productSuccessRate;
        private String productRatingDesc;
        private String productTalkCount;

        private Builder() {
        }

        public static Builder aProductStatistic() {
            return new Builder();
        }

        public Builder setProductSoldCount(String productSoldCount) {
            this.productSoldCount = productSoldCount;
            return this;
        }

        public Builder setProductTransactionCount(String productTransactionCount) {
            this.productTransactionCount = productTransactionCount;
            return this;
        }

        public Builder setProductRatingPoint(String productRatingPoint) {
            this.productRatingPoint = productRatingPoint;
            return this;
        }

        public Builder setProductCancelRate(String productCancelRate) {
            this.productCancelRate = productCancelRate;
            return this;
        }

        public Builder setProductReviewCount(String productReviewCount) {
            this.productReviewCount = productReviewCount;
            return this;
        }

        public Builder setProductViewCount(String productViewCount) {
            this.productViewCount = productViewCount;
            return this;
        }

        public Builder setProductSuccessRate(String productSuccessRate) {
            this.productSuccessRate = productSuccessRate;
            return this;
        }

        public Builder setProductRatingDesc(String productRatingDesc) {
            this.productRatingDesc = productRatingDesc;
            return this;
        }

        public Builder setProductTalkCount(String productTalkCount) {
            this.productTalkCount = productTalkCount;
            return this;
        }

        public Builder but() {
            return aProductStatistic().setProductSoldCount(productSoldCount).setProductTransactionCount(productTransactionCount).setProductRatingPoint(productRatingPoint).setProductCancelRate(productCancelRate).setProductReviewCount(productReviewCount).setProductViewCount(productViewCount).setProductSuccessRate(productSuccessRate).setProductRatingDesc(productRatingDesc).setProductTalkCount(productTalkCount);
        }

        public ProductStatistic build() {
            ProductStatistic productStatistic = new ProductStatistic();
            productStatistic.setProductSoldCount(productSoldCount);
            productStatistic.setProductTransactionCount(productTransactionCount);
            productStatistic.setProductRatingPoint(productRatingPoint);
            productStatistic.setProductCancelRate(productCancelRate);
            productStatistic.setProductReviewCount(productReviewCount);
            productStatistic.setProductViewCount(productViewCount);
            productStatistic.setProductSuccessRate(productSuccessRate);
            productStatistic.setProductRatingDesc(productRatingDesc);
            productStatistic.setProductTalkCount(productTalkCount);
            return productStatistic;
        }
    }
}
