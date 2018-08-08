package com.tokopedia.core.talkview.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReputationBadge implements Parcelable {

    @SerializedName("level")
    @Expose
    private int level;
    @SerializedName("set")
    @Expose
    private int set;

    /**
     *
     * @return
     *     The level
     */
    public int getLevel() {
        return level;
    }

    /**
     *
     * @param level
     *     The level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     *
     * @return
     *     The set
     */
    public int getSet() {
        return set;
    }

    /**
     *
     * @param set
     *     The set
     */
    public void setSet(int set) {
        this.set = set;
    }


    protected ReputationBadge(Parcel in) {
        level = in.readInt();
        set = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(level);
        dest.writeInt(set);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReputationBadge> CREATOR = new Parcelable.Creator<ReputationBadge>() {
        @Override
        public ReputationBadge createFromParcel(Parcel in) {
            return new ReputationBadge(in);
        }

        @Override
        public ReputationBadge[] newArray(int size) {
            return new ReputationBadge[size];
        }
    };
}