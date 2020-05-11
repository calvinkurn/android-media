package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/21/17.
 */

public class DetailCancelRequestViewModel implements Parcelable{
    private int cancelRequest;
    private String reasonTime;
    private String reason;

    public DetailCancelRequestViewModel() {
    }

    protected DetailCancelRequestViewModel(Parcel in) {
        cancelRequest = in.readInt();
        reasonTime = in.readString();
        reason = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cancelRequest);
        dest.writeString(reasonTime);
        dest.writeString(reason);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DetailCancelRequestViewModel> CREATOR = new Creator<DetailCancelRequestViewModel>() {
        @Override
        public DetailCancelRequestViewModel createFromParcel(Parcel in) {
            return new DetailCancelRequestViewModel(in);
        }

        @Override
        public DetailCancelRequestViewModel[] newArray(int size) {
            return new DetailCancelRequestViewModel[size];
        }
    };

    public int getCancelRequest() {
        return cancelRequest;
    }

    public void setCancelRequest(int cancelRequest) {
        this.cancelRequest = cancelRequest;
    }

    public String getReasonTime() {
        return reasonTime;
    }

    public void setReasonTime(String reasonTime) {
        this.reasonTime = reasonTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
