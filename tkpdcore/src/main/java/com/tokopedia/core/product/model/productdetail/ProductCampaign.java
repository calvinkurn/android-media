package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brilliant.oka on 29/03/17.
 */

public class ProductCampaign implements Parcelable {
    @SerializedName("percentage")
    @Expose
    private int percentage;
    @SerializedName("discounted_price")
    @Expose
    private String discountedPrice;
    @SerializedName("expired_time")
    @Expose
    private String expiredTime;
    @SerializedName("original_price")
    @Expose
    private String originalPrice;

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.percentage);
        dest.writeString(this.discountedPrice);
        dest.writeString(this.expiredTime);
        dest.writeString(this.originalPrice);
    }

    public ProductCampaign() {
    }

    protected ProductCampaign(Parcel in) {
        this.percentage = in.readInt();
        this.discountedPrice = in.readString();
        this.expiredTime = in.readString();
        this.originalPrice = in.readString();
    }

    public static final Parcelable.Creator<ProductCampaign> CREATOR = new Parcelable.Creator<ProductCampaign>() {
        @Override
        public ProductCampaign createFromParcel(Parcel source) {
            return new ProductCampaign(source);
        }

        @Override
        public ProductCampaign[] newArray(int size) {
            return new ProductCampaign[size];
        }
    };
}
