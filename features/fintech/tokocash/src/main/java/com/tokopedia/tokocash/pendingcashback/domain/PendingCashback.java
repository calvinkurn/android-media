package com.tokopedia.tokocash.pendingcashback.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class PendingCashback implements Parcelable {

    private int amount;

    private String amountText;

    public PendingCashback() {
    }

    protected PendingCashback(Parcel in) {
        amount = in.readInt();
        amountText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(amount);
        dest.writeString(amountText);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PendingCashback> CREATOR = new Creator<PendingCashback>() {
        @Override
        public PendingCashback createFromParcel(Parcel in) {
            return new PendingCashback(in);
        }

        @Override
        public PendingCashback[] newArray(int size) {
            return new PendingCashback[size];
        }
    };

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAmountText() {
        return amountText;
    }

    public void setAmountText(String amountText) {
        this.amountText = amountText;
    }
}
