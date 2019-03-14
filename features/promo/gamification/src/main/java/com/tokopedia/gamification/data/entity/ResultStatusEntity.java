package com.tokopedia.gamification.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class ResultStatusEntity implements Parcelable {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("message")
    @Expose
    private List<String> message;

    @SerializedName("status")
    @Expose
    private String status;

    public ResultStatusEntity() {
    }

    protected ResultStatusEntity(Parcel in) {
        code = in.readString();
        message = in.createStringArrayList();
        status = in.readString();
    }

    public static final Creator<ResultStatusEntity> CREATOR = new Creator<ResultStatusEntity>() {
        @Override
        public ResultStatusEntity createFromParcel(Parcel in) {
            return new ResultStatusEntity(in);
        }

        @Override
        public ResultStatusEntity[] newArray(int size) {
            return new ResultStatusEntity[size];
        }
    };

    public String getCode() {
        return code;
    }

    public List<String> getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeStringList(message);
        dest.writeString(status);
    }
}
