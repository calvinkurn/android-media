
package com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewResponse implements Parcelable{

    @SerializedName("response_time_ago")
    @Expose
    String responseTimeAgo;
    @SerializedName("response_msg")
    @Expose
    String responseMsg;
    @SerializedName("response_time_fmt")
    @Expose
    String responseTimeFmt;

    protected ReviewResponse(Parcel in) {
        responseTimeAgo = in.readString();
        responseMsg = in.readString();
        responseTimeFmt = in.readString();
    }

    public static final Creator<ReviewResponse> CREATOR = new Creator<ReviewResponse>() {
        @Override
        public ReviewResponse createFromParcel(Parcel in) {
            return new ReviewResponse(in);
        }

        @Override
        public ReviewResponse[] newArray(int size) {
            return new ReviewResponse[size];
        }
    };

    public String getResponseTimeAgo() {
        return responseTimeAgo;
    }

    public void setResponseTimeAgo(String responseTimeAgo) {
        this.responseTimeAgo = responseTimeAgo;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getResponseTimeFmt() {
        return responseTimeFmt;
    }

    public void setResponseTimeFmt(String responseTimeFmt) {
        this.responseTimeFmt = responseTimeFmt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(responseTimeAgo);
        dest.writeString(responseMsg);
        dest.writeString(responseTimeFmt);
    }
}
