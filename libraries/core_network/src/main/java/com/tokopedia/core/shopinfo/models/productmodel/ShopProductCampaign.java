package com.tokopedia.core.shopinfo.models.productmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by brilliant.oka on 29/03/17.
 */

@Deprecated
public class ShopProductCampaign implements Parcelable {

    @SerializedName("product_id")
    @Expose
    private int productId;

    @SerializedName("percentage_amount")
    @Expose
    private int percentageAmount;

    @SerializedName("discounted_price")
    @Expose
    private String discountedPrice;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("original_price")
    @Expose
    private String originalPrice;

    @SerializedName("original_price_idr")
    @Expose
    private String originalPriceIdr;

    @SerializedName("discounted_price_idr")
    @Expose
    private String discountedPriceIdr;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPercentageAmount() {
        return percentageAmount;
    }

    public void setPercentageAmount(int percentageAmount) {
        this.percentageAmount = percentageAmount;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
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

    public String getOriginalPriceIdr() {
        return originalPriceIdr;
    }

    public void setOriginalPriceIdr(String originalPriceIdr) {
        this.originalPriceIdr = originalPriceIdr;
    }

    public String getDiscountedPriceIdr() {
        return discountedPriceIdr;
    }

    public void setDiscountedPriceIdr(String discountedPriceIdr) {
        this.discountedPriceIdr = discountedPriceIdr;
    }


    public ShopProductCampaign() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productId);
        dest.writeInt(this.percentageAmount);
        dest.writeString(this.discountedPrice);
        dest.writeString(this.endDate);
        dest.writeString(this.originalPrice);
        dest.writeString(this.originalPriceIdr);
        dest.writeString(this.discountedPriceIdr);
    }

    protected ShopProductCampaign(Parcel in) {
        this.productId = in.readInt();
        this.percentageAmount = in.readInt();
        this.discountedPrice = in.readString();
        this.endDate = in.readString();
        this.originalPrice = in.readString();
        this.originalPriceIdr = in.readString();
        this.discountedPriceIdr = in.readString();
    }

    public static final Creator<ShopProductCampaign> CREATOR = new Creator<ShopProductCampaign>() {
        @Override
        public ShopProductCampaign createFromParcel(Parcel source) {
            return new ShopProductCampaign(source);
        }

        @Override
        public ShopProductCampaign[] newArray(int size) {
            return new ShopProductCampaign[size];
        }
    };
}
