
package com.tokopedia.challenges.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmissionCounts implements Parcelable{

    @SerializedName("Approved")
    @Expose
    private int approved;
    @SerializedName("Waiting")
    @Expose
    private int waiting;
    @SerializedName("Declined")
    @Expose
    private int declined;

    protected SubmissionCounts(Parcel in) {
        approved = in.readInt();
        waiting = in.readInt();
        declined = in.readInt();
    }

    public static final Creator<SubmissionCounts> CREATOR = new Creator<SubmissionCounts>() {
        @Override
        public SubmissionCounts createFromParcel(Parcel in) {
            return new SubmissionCounts(in);
        }

        @Override
        public SubmissionCounts[] newArray(int size) {
            return new SubmissionCounts[size];
        }
    };

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    public int getDeclined() {
        return declined;
    }

    public void setDeclined(int declined) {
        this.declined = declined;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(approved);
        parcel.writeInt(waiting);
        parcel.writeInt(declined);
    }
}
