
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

public class ReviewResponse implements Parcelable{

    @SerializedName("response_create_time")
    @Expose
    private String responseCreateTime;
    @SerializedName("response_message")
    @Expose
    private String responseMessage;

    protected ReviewResponse(Parcel in) {
        responseCreateTime = in.readString();
        responseMessage = in.readString();
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

    /**
     * 
     * @return
     *     The responseCreateTime
     */
    public String getResponseCreateTime() {
        return responseCreateTime;
    }

    /**
     * 
     * @param responseCreateTime
     *     The response_create_time
     */
    public void setResponseCreateTime(String responseCreateTime) {
        this.responseCreateTime = responseCreateTime;
    }

    /**
     * 
     * @return
     *     The responseMessage
     */
    public Spanned getResponseMessage() {
        return MethodChecker.fromHtml(responseMessage);
    }

    /**
     * 
     * @param responseMessage
     *     The response_message
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(responseCreateTime);
        dest.writeString(responseMessage);
    }
}
