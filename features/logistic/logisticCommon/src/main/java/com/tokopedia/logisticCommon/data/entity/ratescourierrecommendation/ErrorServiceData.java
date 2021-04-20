package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 11/10/18.
 */

public class ErrorServiceData implements Parcelable {

    @SerializedName("error_id")
    @Expose
    private String errorId;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;

    public ErrorServiceData() {
    }

    protected ErrorServiceData(Parcel in) {
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

    public static final Creator<ErrorServiceData> CREATOR = new Creator<ErrorServiceData>() {
        @Override
        public ErrorServiceData createFromParcel(Parcel in) {
            return new ErrorServiceData(in);
        }

        @Override
        public ErrorServiceData[] newArray(int size) {
            return new ErrorServiceData[size];
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
