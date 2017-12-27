
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReputation implements Parcelable {

    @SerializedName("positive")
    @Expose
    private int positive;
    @SerializedName("neutral")
    @Expose
    private int neutral;
    @SerializedName("negative")
    @Expose
    private int negative;
    @SerializedName("positive_percentage")
    @Expose
    private String positivePercentage;
    @SerializedName("no_reputation")
    @Expose
    private int noReputation;

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getNeutral() {
        return neutral;
    }

    public void setNeutral(int neutral) {
        this.neutral = neutral;
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }

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

    public UserReputation() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.positive);
        dest.writeInt(this.neutral);
        dest.writeInt(this.negative);
        dest.writeString(this.positivePercentage);
        dest.writeInt(this.noReputation);
    }

    protected UserReputation(Parcel in) {
        this.positive = in.readInt();
        this.neutral = in.readInt();
        this.negative = in.readInt();
        this.positivePercentage = in.readString();
        this.noReputation = in.readInt();
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
