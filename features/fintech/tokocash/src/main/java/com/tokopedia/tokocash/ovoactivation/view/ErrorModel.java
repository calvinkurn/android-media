package com.tokopedia.tokocash.ovoactivation.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 10/10/18.
 */
public class ErrorModel implements Parcelable {

    private String message;

    public ErrorModel() {
    }

    protected ErrorModel(Parcel in) {
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ErrorModel> CREATOR = new Creator<ErrorModel>() {
        @Override
        public ErrorModel createFromParcel(Parcel in) {
            return new ErrorModel(in);
        }

        @Override
        public ErrorModel[] newArray(int size) {
            return new ErrorModel[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
