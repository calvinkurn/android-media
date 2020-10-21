package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.helpful_review;

/**
 * Created by stevenfredian on 3/1/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewUserReputation implements Parcelable{

    @SerializedName("positive_percentage")
    @Expose
    private String positivePercentage;
    @SerializedName("no_reputation")
    @Expose
    private int noReputation;
    @SerializedName("negative")
    @Expose
    private String negative;
    @SerializedName("positive")
    @Expose
    private String positive;
    @SerializedName("neutral")
    @Expose
    private String neutral;

    protected ReviewUserReputation(Parcel in) {
        positivePercentage = in.readString();
        noReputation = in.readInt();
        negative = in.readString();
        positive = in.readString();
        neutral = in.readString();
    }

    public static final Creator<ReviewUserReputation> CREATOR = new Creator<ReviewUserReputation>() {
        @Override
        public ReviewUserReputation createFromParcel(Parcel in) {
            return new ReviewUserReputation(in);
        }

        @Override
        public ReviewUserReputation[] newArray(int size) {
            return new ReviewUserReputation[size];
        }
    };

    /**
     * @return The positivePercentage
     */
    public String getPositivePercentage() {
        return positivePercentage;
    }

    /**
     * @param positivePercentage The positive_percentage
     */
    public void setPositivePercentage(String positivePercentage) {
        this.positivePercentage = positivePercentage;
    }

    /**
     * @return The noReputation
     */
    public int getNoReputation() {
        return noReputation;
    }

    /**
     * @param noReputation The no_reputation
     */
    public void setNoReputation(int noReputation) {
        this.noReputation = noReputation;
    }

    /**
     * @return The negative
     */
    public String getNegative() {
        return negative;
    }

    /**
     * @param negative The negative
     */
    public void setNegative(String negative) {
        this.negative = negative;
    }

    /**
     * @return The positive
     */
    public String getPositive() {
        return positive;
    }

    /**
     * @param positive The positive
     */
    public void setPositive(String positive) {
        this.positive = positive;
    }

    /**
     * @return The neutral
     */
    public String getNeutral() {
        return neutral;
    }

    /**
     * @param neutral The neutral
     */
    public void setNeutral(String neutral) {
        this.neutral = neutral;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(positivePercentage);
        dest.writeInt(noReputation);
        dest.writeString(negative);
        dest.writeString(positive);
        dest.writeString(neutral);
    }
}
