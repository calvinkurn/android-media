package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class CashBackData implements Parcelable {

    private int amount;

    private String amountText;

    public CashBackData() {
    }

    protected CashBackData(Parcel in) {
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

    public static final Creator<CashBackData> CREATOR = new Creator<CashBackData>() {
        @Override
        public CashBackData createFromParcel(Parcel in) {
            return new CashBackData(in);
        }

        @Override
        public CashBackData[] newArray(int size) {
            return new CashBackData[size];
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
