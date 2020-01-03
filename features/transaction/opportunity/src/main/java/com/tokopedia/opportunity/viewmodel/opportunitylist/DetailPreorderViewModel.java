package com.tokopedia.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/21/17.
 */

public class DetailPreorderViewModel implements Parcelable{
    private int preorderStatus;
    private String preorderProcessTimeType;
    private String preorderProcessTimeTypeString;
    private String preorderProcessTime;

    public DetailPreorderViewModel() {
    }

    protected DetailPreorderViewModel(Parcel in) {
        preorderStatus = in.readInt();
        preorderProcessTimeType = in.readString();
        preorderProcessTimeTypeString = in.readString();
        preorderProcessTime = in.readString();
    }

    public static final Creator<DetailPreorderViewModel> CREATOR = new Creator<DetailPreorderViewModel>() {
        @Override
        public DetailPreorderViewModel createFromParcel(Parcel in) {
            return new DetailPreorderViewModel(in);
        }

        @Override
        public DetailPreorderViewModel[] newArray(int size) {
            return new DetailPreorderViewModel[size];
        }
    };

    public int getPreorderStatus() {
        return preorderStatus;
    }

    public void setPreorderStatus(int preorderStatus) {
        this.preorderStatus = preorderStatus;
    }

    public String getPreorderProcessTimeType() {
        return preorderProcessTimeType;
    }

    public void setPreorderProcessTimeType(String preorderProcessTimeType) {
        this.preorderProcessTimeType = preorderProcessTimeType;
    }

    public String getPreorderProcessTimeTypeString() {
        return preorderProcessTimeTypeString;
    }

    public void setPreorderProcessTimeTypeString(String preorderProcessTimeTypeString) {
        this.preorderProcessTimeTypeString = preorderProcessTimeTypeString;
    }

    public String getPreorderProcessTime() {
        return preorderProcessTime;
    }

    public void setPreorderProcessTime(String preorderProcessTime) {
        this.preorderProcessTime = preorderProcessTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(preorderStatus);
        dest.writeString(preorderProcessTimeType);
        dest.writeString(preorderProcessTimeTypeString);
        dest.writeString(preorderProcessTime);
    }
}
