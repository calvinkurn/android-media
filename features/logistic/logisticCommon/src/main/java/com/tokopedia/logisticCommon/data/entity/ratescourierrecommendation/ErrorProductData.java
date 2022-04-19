package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ErrorProductData implements Parcelable {

    public static final String ERROR_PINPOINT_NEEDED = "501";
    public static final String ERROR_DISTANCE_LIMIT_EXCEEDED = "502";
    public static final String ERROR_WEIGHT_LIMIT_EXCEEDED = "503";
    public static final String ERROR_RATES_NOT_AVAILABLE = "504";

    @SerializedName("error_id")
    @Expose
    private String errorId;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;

    public ErrorProductData() {
    }

    protected ErrorProductData(Parcel in) {
        errorId = in.readString();
        errorMessage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(errorId);
        dest.writeString(errorMessage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ErrorProductData> CREATOR = new Creator<ErrorProductData>() {
        @Override
        public ErrorProductData createFromParcel(Parcel in) {
            return new ErrorProductData(in);
        }

        @Override
        public ErrorProductData[] newArray(int size) {
            return new ErrorProductData[size];
        }
    };

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
