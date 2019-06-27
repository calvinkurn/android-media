package com.tokopedia.transactiondata.entity.response.cod;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 19/12/18.
 */
public class PriceSummary implements Parcelable {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("label_info")
    @Expose
    private String labelInfo;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("price_fmt")
    @Expose
    private String priceFmt;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelInfo() {
        return labelInfo;
    }

    public void setLabelInfo(String labelInfo) {
        this.labelInfo = labelInfo;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPriceFmt() {
        return priceFmt;
    }

    public void setPriceFmt(String priceFmt) {
        this.priceFmt = priceFmt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeString(this.labelInfo);
        dest.writeValue(this.price);
        dest.writeString(this.priceFmt);
    }

    public PriceSummary() {
    }

    protected PriceSummary(Parcel in) {
        this.label = in.readString();
        this.labelInfo = in.readString();
        this.price = (Integer) in.readValue(Integer.class.getClassLoader());
        this.priceFmt = in.readString();
    }

    public static final Parcelable.Creator<PriceSummary> CREATOR = new Parcelable.Creator<PriceSummary>() {
        @Override
        public PriceSummary createFromParcel(Parcel source) {
            return new PriceSummary(source);
        }

        @Override
        public PriceSummary[] newArray(int size) {
            return new PriceSummary[size];
        }
    };
}
