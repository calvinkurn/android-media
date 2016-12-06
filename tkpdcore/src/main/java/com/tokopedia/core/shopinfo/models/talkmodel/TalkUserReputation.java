
package com.tokopedia.core.shopinfo.models.talkmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TalkUserReputation implements Parcelable{

    @SerializedName("positive_percentage")
    @Expose
    public String positivePercentage;
    @SerializedName("no_reputation")
    @Expose
    public int noReputation;
    @SerializedName("negative")
    @Expose
    public String negative;
    @SerializedName("positive")
    @Expose
    public String positive;
    @SerializedName("neutral")
    @Expose
    public String neutral;

    public String getPositivePercentage() {
        return positivePercentage;
    }

    public void setPositivePercentage(String positivePercentage) {
        this.positivePercentage = positivePercentage;
    }

    public int getNoReputation() {
        return noReputation;
    }

    public void setNoReputation(int noReputation) {
        this.noReputation = noReputation;
    }

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    public String getNeutral() {
        return neutral;
    }

    public void setNeutral(String neutral) {
        this.neutral = neutral;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.positivePercentage);
        dest.writeInt(this.noReputation);
        dest.writeString(this.negative);
        dest.writeString(this.positive);
        dest.writeString(this.neutral);
    }

    public TalkUserReputation() {
    }

    protected TalkUserReputation(Parcel in) {
        this.positivePercentage = in.readString();
        this.noReputation = in.readInt();
        this.negative = in.readString();
        this.positive = in.readString();
        this.neutral = in.readString();
    }

    public static final Creator<TalkUserReputation> CREATOR = new Creator<TalkUserReputation>() {
        @Override
        public TalkUserReputation createFromParcel(Parcel source) {
            return new TalkUserReputation(source);
        }

        @Override
        public TalkUserReputation[] newArray(int size) {
            return new TalkUserReputation[size];
        }
    };
}
