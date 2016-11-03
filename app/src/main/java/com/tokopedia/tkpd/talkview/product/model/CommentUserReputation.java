package com.tokopedia.tkpd.talkview.product.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentUserReputation implements Parcelable {

    @SerializedName("no_reputation")
    @Expose
    private int noReputation;
    @SerializedName("negative")
    @Expose
    private String negative;
    @SerializedName("positive")
    @Expose
    private String positive;
    @SerializedName("positive_percentage")
    @Expose
    private String positivePercentage;
    @SerializedName("neutral")
    @Expose
    private String neutral;

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


    protected CommentUserReputation(Parcel in) {
        noReputation = in.readInt();
        negative = in.readString();
        positive = in.readString();
        positivePercentage = in.readString();
        neutral = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(noReputation);
        dest.writeString(negative);
        dest.writeString(positive);
        dest.writeString(positivePercentage);
        dest.writeString(neutral);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CommentUserReputation> CREATOR = new Parcelable.Creator<CommentUserReputation>() {
        @Override
        public CommentUserReputation createFromParcel(Parcel in) {
            return new CommentUserReputation(in);
        }

        @Override
        public CommentUserReputation[] newArray(int size) {
            return new CommentUserReputation[size];
        }
    };
}