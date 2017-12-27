
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewResponse implements Parcelable {

    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("response_time")
    @Expose
    private ResponseTime responseTime;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ResponseTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(ResponseTime responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.responseMessage);
        dest.writeParcelable(this.responseTime, flags);
    }

    public ReviewResponse() {
    }

    protected ReviewResponse(Parcel in) {
        this.responseMessage = in.readString();
        this.responseTime = in.readParcelable(ResponseTime.class.getClassLoader());
    }

    public static final Parcelable.Creator<ReviewResponse> CREATOR = new Parcelable.Creator<ReviewResponse>() {
        @Override
        public ReviewResponse createFromParcel(Parcel source) {
            return new ReviewResponse(source);
        }

        @Override
        public ReviewResponse[] newArray(int size) {
            return new ReviewResponse[size];
        }
    };
}
