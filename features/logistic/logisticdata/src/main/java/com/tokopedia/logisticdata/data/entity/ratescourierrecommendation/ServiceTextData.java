package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ServiceTextData implements Parcelable {

    @SerializedName("text_range_price")
    @Expose
    private String textRangePrice;
    @SerializedName("text_etd")
    @Expose
    private String textEtd;
    @SerializedName("text_service_desc")
    @Expose
    private String textServiceDesc;

    public ServiceTextData() {
    }

    protected ServiceTextData(Parcel in) {
        textRangePrice = in.readString();
        textEtd = in.readString();
        textServiceDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(textRangePrice);
        dest.writeString(textEtd);
        dest.writeString(textServiceDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceTextData> CREATOR = new Creator<ServiceTextData>() {
        @Override
        public ServiceTextData createFromParcel(Parcel in) {
            return new ServiceTextData(in);
        }

        @Override
        public ServiceTextData[] newArray(int size) {
            return new ServiceTextData[size];
        }
    };

    public String getTextRangePrice() {
        return textRangePrice;
    }

    public void setTextRangePrice(String textRangePrice) {
        this.textRangePrice = textRangePrice;
    }

    public String getTextEtd() {
        return textEtd;
    }

    public void setTextEtd(String textEtd) {
        this.textEtd = textEtd;
    }

    public String getTextServiceDesc() {
        return textServiceDesc;
    }

    public void setTextServiceDesc(String textServiceDesc) {
        this.textServiceDesc = textServiceDesc;
    }
}
