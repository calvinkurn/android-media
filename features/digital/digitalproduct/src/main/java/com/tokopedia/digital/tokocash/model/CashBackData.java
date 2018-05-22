package com.tokopedia.digital.tokocash.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 6/19/17. Tokopedia
 */

public class CashBackData implements Parcelable {

    private int amount;

    private String amountText;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.amount);
        dest.writeString(this.amountText);
    }

    public CashBackData() {
    }

    protected CashBackData(Parcel in) {
        this.amount = in.readInt();
        this.amountText = in.readString();
    }

    public static final Parcelable.Creator<CashBackData> CREATOR = new Parcelable.Creator<CashBackData>() {
        @Override
        public CashBackData createFromParcel(Parcel source) {
            return new CashBackData(source);
        }

        @Override
        public CashBackData[] newArray(int size) {
            return new CashBackData[size];
        }
    };
}
