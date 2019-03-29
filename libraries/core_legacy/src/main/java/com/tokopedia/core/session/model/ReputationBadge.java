
package com.tokopedia.core.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReputationBadge implements Parcelable {

    @SerializedName("set")
    @Expose
    private int set;
    @SerializedName("level")
    @Expose
    private int level;

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

    public static final Creator<ReputationBadge> CREATOR = new Creator<ReputationBadge>() {
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
