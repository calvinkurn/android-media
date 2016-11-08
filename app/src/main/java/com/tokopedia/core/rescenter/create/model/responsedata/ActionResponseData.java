package com.tokopedia.core.rescenter.create.model.responsedata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 4/14/16.
 */
public class ActionResponseData implements Parcelable {

    @SerializedName("post_key")
    @Expose
    private String postKey;
    @SerializedName("is_success")
    @Expose
    private Integer success;
    @SerializedName("resolution_id")
    @Expose
    private String resolutionID;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return getSuccess() == 1;
    }

    public String getResolutionID() {
        return resolutionID;
    }

    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.postKey);
        dest.writeValue(this.success);
        dest.writeString(this.resolutionID);
    }

    public ActionResponseData() {
    }

    protected ActionResponseData(Parcel in) {
        this.postKey = in.readString();
        this.success = (Integer) in.readValue(Integer.class.getClassLoader());
        this.resolutionID = in.readString();
    }

    public static final Creator<ActionResponseData> CREATOR = new Creator<ActionResponseData>() {
        @Override
        public ActionResponseData createFromParcel(Parcel source) {
            return new ActionResponseData(source);
        }

        @Override
        public ActionResponseData[] newArray(int size) {
            return new ActionResponseData[size];
        }
    };
}
