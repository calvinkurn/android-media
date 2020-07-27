package com.tokopedia.core.session.model;

/**
 * Created by stevenfredian on 8/12/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReputation implements Parcelable {

    @SerializedName("no_reputation")
    @Expose
    private int noReputation;
    @SerializedName("negative")
    @Expose
    private String negative;
    @SerializedName("neutral")
    @Expose
    private String neutral;
    @SerializedName("positive")
    @Expose
    private String positive;
    @SerializedName("positive_percentage")
    @Expose
    private String positivePercentage;

    /**
     *
     * @return
     *     The noReputation
     */
    public int getNoReputation() {
        return noReputation;
    }

    /**
     *
     * @param noReputation
     *     The no_reputation
     */
    public void setNoReputation(int noReputation) {
        this.noReputation = noReputation;
    }

    /**
     *
     * @return
     *     The negative
     */
    public String getNegative() {
        return negative;
    }

    /**
     *
     * @param negative
     *     The negative
     */
    public void setNegative(String negative) {
        this.negative = negative;
    }

    /**
     *
     * @return
     *     The neutral
     */
    public String getNeutral() {
        return neutral;
    }

    /**
     *
     * @param neutral
     *     The neutral
     */
    public void setNeutral(String neutral) {
        this.neutral = neutral;
    }

    /**
     *
     * @return
     *     The positive
     */
    public String getPositive() {
        return positive;
    }

    /**
     *
     * @param positive
     *     The positive
     */
    public void setPositive(String positive) {
        this.positive = positive;
    }

    /**
     *
     * @return
     *     The positivePercentage
     */
    public String getPositivePercentage() {
        return positivePercentage;
    }

    /**
     *
     * @param positivePercentage
     *     The positive_percentage
     */
    public void setPositivePercentage(String positivePercentage) {
        this.positivePercentage = positivePercentage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.noReputation);
        dest.writeString(this.negative);
        dest.writeString(this.neutral);
        dest.writeString(this.positive);
        dest.writeString(this.positivePercentage);
    }

    public UserReputation() {
    }

    protected UserReputation(Parcel in) {
        this.noReputation = in.readInt();
        this.negative = in.readString();
        this.neutral = in.readString();
        this.positive = in.readString();
        this.positivePercentage = in.readString();
    }

    public static final Creator<UserReputation> CREATOR = new Creator<UserReputation>() {
        @Override
        public UserReputation createFromParcel(Parcel source) {
            return new UserReputation(source);
        }

        @Override
        public UserReputation[] newArray(int size) {
            return new UserReputation[size];
        }
    };
}

