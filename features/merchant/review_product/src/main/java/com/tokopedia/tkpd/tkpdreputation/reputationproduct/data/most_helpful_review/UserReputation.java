
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.most_helpful_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReputation implements Parcelable{

    @SerializedName("negative")
    @Expose
    private String negative;
    @SerializedName("neutral")
    @Expose
    private String neutral;
    @SerializedName("positive_percentage")
    @Expose
    private String positivePercentage;
    @SerializedName("positive")
    @Expose
    private String positive;
    @SerializedName("no_reputation")
    @Expose
    private int noReputation;

    protected UserReputation(Parcel in) {
        negative = in.readString();
        neutral = in.readString();
        positivePercentage = in.readString();
        positive = in.readString();
        noReputation = in.readInt();
    }

    public static final Creator<UserReputation> CREATOR = new Creator<UserReputation>() {
        @Override
        public UserReputation createFromParcel(Parcel in) {
            return new UserReputation(in);
        }

        @Override
        public UserReputation[] newArray(int size) {
            return new UserReputation[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(negative);
        dest.writeString(neutral);
        dest.writeString(positivePercentage);
        dest.writeString(positive);
        dest.writeInt(noReputation);
    }
}
