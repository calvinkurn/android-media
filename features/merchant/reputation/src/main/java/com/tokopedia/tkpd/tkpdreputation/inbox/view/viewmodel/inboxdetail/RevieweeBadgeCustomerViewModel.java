package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/30/17.
 */

public class RevieweeBadgeCustomerViewModel implements Parcelable{
    private int positive;
    private int neutral;
    private int negative;
    private String positivePercentage;
    private int noReputation;

    public RevieweeBadgeCustomerViewModel(int positive, int neutral, int negative,
                                          String positivePercentage, int noReputation) {
        this.positive = positive;
        this.neutral = neutral;
        this.negative = negative;
        this.positivePercentage = positivePercentage;
        this.noReputation = noReputation;
    }

    protected RevieweeBadgeCustomerViewModel(Parcel in) {
        positive = in.readInt();
        neutral = in.readInt();
        negative = in.readInt();
        positivePercentage = in.readString();
        noReputation = in.readInt();
    }

    public static final Creator<RevieweeBadgeCustomerViewModel> CREATOR = new Creator<RevieweeBadgeCustomerViewModel>() {
        @Override
        public RevieweeBadgeCustomerViewModel createFromParcel(Parcel in) {
            return new RevieweeBadgeCustomerViewModel(in);
        }

        @Override
        public RevieweeBadgeCustomerViewModel[] newArray(int size) {
            return new RevieweeBadgeCustomerViewModel[size];
        }
    };

    public int getPositive() {
        return positive;
    }

    public int getNeutral() {
        return neutral;
    }

    public int getNegative() {
        return negative;
    }

    public String getPositivePercentage() {
        return positivePercentage;
    }

    public int getNoReputation() {
        return noReputation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(positive);
        dest.writeInt(neutral);
        dest.writeInt(negative);
        dest.writeString(positivePercentage);
        dest.writeInt(noReputation);
    }
}
