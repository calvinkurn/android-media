package com.tokopedia.tkpd.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by stevenfredian on 5/30/16.
 */
public class ErrorModel implements Parcelable{

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("error_description")
    @Expose
    private String error_description;

    @SerializedName("state")
    @Expose
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.error);
        dest.writeString(this.error_description);
        dest.writeString(this.state);
    }

    public ErrorModel() {
    }

    protected ErrorModel(Parcel in) {
        this.error = in.readString();
        this.error_description = in.readString();
        this.state = in.readString();
    }

    public static final Parcelable.Creator<ErrorModel> CREATOR = new Parcelable.Creator<ErrorModel>() {
        @Override
        public ErrorModel createFromParcel(Parcel source) {
            return new ErrorModel(source);
        }

        @Override
        public ErrorModel[] newArray(int size) {
            return new ErrorModel[size];
        }
    };
}
