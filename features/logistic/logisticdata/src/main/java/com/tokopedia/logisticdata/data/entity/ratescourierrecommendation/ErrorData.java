package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ErrorData implements Parcelable {

    @SerializedName("error_id")
    @Expose
    private String errorId;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;

    public ErrorData() {
    }

    protected ErrorData(Parcel in) {
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

    public static final Creator<ErrorData> CREATOR = new Creator<ErrorData>() {
        @Override
        public ErrorData createFromParcel(Parcel in) {
            return new ErrorData(in);
        }

        @Override
        public ErrorData[] newArray(int size) {
            return new ErrorData[size];
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
