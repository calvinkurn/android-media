package com.tokopedia.transactiondata.entity.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EgoldData implements Parcelable {
    @SerializedName("is_egold")
    @Expose
    public boolean isEgold;

    @SerializedName("gold_amount")
    @Expose
    public long egoldAmount;

    public EgoldData() {
    }

    protected EgoldData(Parcel in) {
        isEgold = in.readByte() != 0;
        egoldAmount = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isEgold ? 1 : 0));
        dest.writeLong(egoldAmount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EgoldData> CREATOR = new Creator<EgoldData>() {
        @Override
        public EgoldData createFromParcel(Parcel in) {
            return new EgoldData(in);
        }

        @Override
        public EgoldData[] newArray(int size) {
            return new EgoldData[size];
        }
    };

    public boolean isEgold() {
        return isEgold;
    }

    public void setEgold(boolean egold) {
        isEgold = egold;
    }

    public long getEgoldAmount() {
        return egoldAmount;
    }

    public void setEgoldAmount(long egoldAmount) {
        this.egoldAmount = egoldAmount;
    }
}
