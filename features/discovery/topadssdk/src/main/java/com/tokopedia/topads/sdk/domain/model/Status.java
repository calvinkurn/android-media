package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class Status implements Parcelable {

    @SerializedName(KEY_ERROR_CODE)
    private int errorCode;
    @SerializedName(KEY_MESSAGE)
    private String message = "";

    private static final String KEY_ERROR_CODE = "error_code";
    private static final String KEY_MESSAGE = "message";

    public Status() {
    }

    public Status(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ERROR_CODE)) {
            setErrorCode(object.getInt(KEY_ERROR_CODE));
        }
        if(!object.isNull(KEY_MESSAGE)) {
            setMessage(object.getString(KEY_MESSAGE));
        }
    }

    public Status(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    protected Status(Parcel in) {
        errorCode = in.readInt();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(errorCode);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
