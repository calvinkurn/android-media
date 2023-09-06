package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ServiceTextData implements Parcelable {

    @SerializedName("text_range_price")
    private String textRangePrice;

    @SerializedName("text_etd")
    private String textEtd;

    @SerializedName("text_service_desc")
    private String textServiceDesc;

    @SerializedName("text_eta_summarize")
    private String textEtaSummarize;

    @SerializedName("error_code")
    private int errorCode;

    @SerializedName("text_service_ticker")
    private String textServiceTicker;

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

    public String getTextEtaSummarize() {
        return textEtaSummarize;
    }

    public void setTextEtaSummarize(String textEtaSummarize) {
        this.textEtaSummarize = textEtaSummarize;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getTextServiceTicker() {
        return textServiceTicker;
    }

    public void setTextServiceTicker(String textServiceTicker) {
        this.textServiceTicker = textServiceTicker;
    }
}
