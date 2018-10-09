package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 20/07/17.
 */

public class ReviewResponseDomain implements Parcelable {

    String responseTimeAgo;
    String responseMsg;
    String responseTimeFmt;

    public ReviewResponseDomain() {

    }

    protected ReviewResponseDomain(Parcel in) {
        responseTimeAgo = in.readString();
        responseMsg = in.readString();
        responseTimeFmt = in.readString();
    }

    public static final Creator<ReviewResponseDomain> CREATOR = new Creator<ReviewResponseDomain>() {
        @Override
        public ReviewResponseDomain createFromParcel(Parcel in) {
            return new ReviewResponseDomain(in);
        }

        @Override
        public ReviewResponseDomain[] newArray(int size) {
            return new ReviewResponseDomain[size];
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