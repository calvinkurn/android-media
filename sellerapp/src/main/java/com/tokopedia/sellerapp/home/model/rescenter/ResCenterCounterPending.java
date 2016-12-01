package com.tokopedia.sellerapp.home.model.rescenter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 4/13/16.
 */
public class ResCenterCounterPending implements Parcelable {

    @SerializedName("total_list")
    @Expose
    private Integer totalList;
    @SerializedName("pending_days")
    @Expose
    private String pendingDays;

    public ResCenterCounterPending() {
    }

    public Integer getTotalList() {
        return totalList;
    }

    public void setTotalList(Integer totalList) {
        this.totalList = totalList;
    }

    public String getPendingDays() {
        return pendingDays;
    }

    public void setPendingDays(String pendingDays) {
        this.pendingDays = pendingDays;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.totalList);
        dest.writeString(this.pendingDays);
    }

    protected ResCenterCounterPending(Parcel in) {
        this.totalList = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pendingDays = in.readString();
    }

    public static final Creator<ResCenterCounterPending> CREATOR = new Creator<ResCenterCounterPending>() {
        @Override
        public ResCenterCounterPending createFromParcel(Parcel source) {
            return new ResCenterCounterPending(source);
        }

        @Override
        public ResCenterCounterPending[] newArray(int size) {
            return new ResCenterCounterPending[size];
        }
    };
}
