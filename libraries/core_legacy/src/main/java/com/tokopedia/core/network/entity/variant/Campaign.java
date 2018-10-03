package com.tokopedia.core.network.entity.variant;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alifa on 2/3/18.
 */

public class Campaign implements Parcelable {

    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("discounted_percentage")
    @Expose
    private int discountedPercentage;
    @SerializedName("discounted_price")
    @Expose
    private int discountedPrice;
    @SerializedName("discounted_price_fmt")
    @Expose
    private String discountedPriceFmt;
    @SerializedName("campaign_type")
    @Expose
    private int campaignType;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("original_price")
    @Expose
    private String originalPrice;
    @SerializedName("original_price_fmt")
    @Expose
    private String originalPriceFmt;
    @SerializedName(value = "campaign_short_name", alternate = "campaign_type_name")
    @Expose
    private String campaignShortName;
    @SerializedName("applinks")
    @Expose
    private String applinks;

    public String getCampaignShortName() {
        return campaignShortName;
    }

    public void setCampaignShortName(String campaignShortName) {
        this.campaignShortName = campaignShortName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public int getDiscountedPercentage() {
        return discountedPercentage;
    }

    public void setDiscountedPercentage(int discountedPercentage) {
        this.discountedPercentage = discountedPercentage;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(int discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getDiscountedPriceFmt() {
        return discountedPriceFmt;
    }

    public void setDiscountedPriceFmt(String discountedPriceFmt) {
        this.discountedPriceFmt = discountedPriceFmt;
    }

    public int getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(int campaignType) {
        this.campaignType = campaignType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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

    public String getOriginalPriceFmt() {
        return originalPriceFmt;
    }

    public void setOriginalPriceFmt(String originalPriceFmt) {
        this.originalPriceFmt = originalPriceFmt;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    protected Campaign(Parcel in) {
        byte isActiveVal = in.readByte();
        isActive = isActiveVal == 0x02 ? null : isActiveVal != 0x00;
        discountedPercentage = in.readInt();
        discountedPrice = in.readInt();
        discountedPriceFmt = in.readString();
        campaignType = in.readInt();
        startDate = in.readString();
        endDate = in.readString();
        originalPrice = in.readString();
        originalPriceFmt = in.readString();
        campaignShortName = in.readString();
        applinks = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (isActive == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isActive ? 0x01 : 0x00));
        }
        dest.writeInt(discountedPercentage);
        dest.writeInt(discountedPrice);
        dest.writeString(discountedPriceFmt);
        dest.writeInt(campaignType);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(originalPrice);
        dest.writeString(originalPriceFmt);
        dest.writeString(campaignShortName);
        dest.writeString(applinks);
    }

    @SuppressWarnings("unused")
    public static final Creator<Campaign> CREATOR = new Creator<Campaign>() {
        @Override
        public Campaign createFromParcel(Parcel in) {
            return new Campaign(in);
        }

        @Override
        public Campaign[] newArray(int size) {
            return new Campaign[size];
        }
    };


}