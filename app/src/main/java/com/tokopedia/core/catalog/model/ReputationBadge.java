package com.tokopedia.core.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class ReputationBadge implements Parcelable {

    @SerializedName("set")
    @Expose
    private int set;
    @SerializedName("level")
    @Expose
    private int level;

    public int getSet() {
        return set;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.set);
        dest.writeInt(this.level);
    }

    public ReputationBadge() {
    }

    protected ReputationBadge(Parcel in) {
        this.set = in.readInt();
        this.level = in.readInt();
    }

    public static final Parcelable.Creator<ReputationBadge> CREATOR = new Parcelable.Creator<ReputationBadge>() {
        @Override
        public ReputationBadge createFromParcel(Parcel source) {
            return new ReputationBadge(source);
        }

        @Override
        public ReputationBadge[] newArray(int size) {
            return new ReputationBadge[size];
        }
    };
}
