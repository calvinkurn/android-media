package com.tokopedia.core.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 4/13/16.
 */
public class ResCenterPendingAmount implements Parcelable {

    @SerializedName("total_amt_idr")
    @Expose
    private String totaAmountIdr;

    public String getTotalAmountIdr() {
        return totaAmountIdr;
    }

    public void setTotalAmountIdr(String totaAmountIdr) {
        this.totaAmountIdr = totaAmountIdr;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.totaAmountIdr);
    }

    public ResCenterPendingAmount() {
    }

    protected ResCenterPendingAmount(Parcel in) {
        this.totaAmountIdr = in.readString();
    }

    public static final Parcelable.Creator<ResCenterPendingAmount> CREATOR = new Parcelable.Creator<ResCenterPendingAmount>() {
        @Override
        public ResCenterPendingAmount createFromParcel(Parcel source) {
            return new ResCenterPendingAmount(source);
        }

        @Override
        public ResCenterPendingAmount[] newArray(int size) {
            return new ResCenterPendingAmount[size];
        }
    };
}
