package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brilliant.oka on 29/03/17.
 */

public class ProductCampaign implements Parcelable {
    @SerializedName("percentage_amount")
    @Expose
    private int percentageAmount;
    @SerializedName("original_price_fmt")
    @Expose
    private String originalPriceFmt;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("original_price")
    @Expose
    private String originalPrice;

    public int getPercentageAmount() {
        return percentageAmount;
    }

    public void setPercentageAmount(int percentageAmount) {
        this.percentageAmount = percentageAmount;
    }

    public String getOriginalPriceFmt() {
        return originalPriceFmt;
    }

    public void setOriginalPriceFmt(String originalPriceFmt) {
        this.originalPriceFmt = originalPriceFmt;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
        dest.writeInt(this.percentageAmount);
        dest.writeString(this.originalPriceFmt);
        dest.writeString(this.endDate);
        dest.writeString(this.originalPrice);
    }

    public ProductCampaign() {
    }

    protected ProductCampaign(Parcel in) {
        this.percentageAmount = in.readInt();
        this.originalPriceFmt = in.readString();
        this.endDate = in.readString();
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
