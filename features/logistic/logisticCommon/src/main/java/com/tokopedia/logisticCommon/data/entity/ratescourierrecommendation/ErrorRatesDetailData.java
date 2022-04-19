package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 11/10/18.
 */

public class ErrorRatesDetailData implements Parcelable {

    @SerializedName("error_id")
    @Expose
    private String errorId;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;

    public ErrorRatesDetailData() {
    }

    protected ErrorRatesDetailData(Parcel in) {
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

    public static final Creator<ErrorRatesDetailData> CREATOR = new Creator<ErrorRatesDetailData>() {
        @Override
        public ErrorRatesDetailData createFromParcel(Parcel in) {
            return new ErrorRatesDetailData(in);
        }

        @Override
        public ErrorRatesDetailData[] newArray(int size) {
            return new ErrorRatesDetailData[size];
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
