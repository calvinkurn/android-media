package com.tokopedia.checkout.view.feature.shipment.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.logisticcart.domain.shipping.ShipmentData;

public class EgoldTieringModel implements ShipmentData, Parcelable {

    private long minTotalAmount;
    private long minAmount;
    private long maxAmount;
    private long basisAmount;

    public EgoldTieringModel() {

    }


    public long getMinTotalAmount() {
        return minTotalAmount;
    }

    public void setMinTotalAmount(long minTotalAmount) {
        this.minTotalAmount = minTotalAmount;
    }

    public long getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(long minAmount) {
        this.minAmount = minAmount;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(long maxAmount) {
        this.maxAmount = maxAmount;
    }

    public long getBasisAmount() {
        return basisAmount;
    }

    public void setBasisAmount(long basisAmount) {
        this.basisAmount = basisAmount;
    }

    public EgoldTieringModel(Parcel in) {
        minTotalAmount = in.readLong();
        minAmount = in.readLong();
        maxAmount = in.readLong();
        basisAmount = in.readLong();
    }

    public static final Creator<EgoldTieringModel> CREATOR = new Creator<EgoldTieringModel>() {
        @Override
        public EgoldTieringModel createFromParcel(Parcel in) {
            return new EgoldTieringModel(in);
        }

        @Override
        public EgoldTieringModel[] newArray(int size) {
            return new EgoldTieringModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(minTotalAmount);
        dest.writeLong(minAmount);
        dest.writeLong(maxAmount);
        dest.writeLong(basisAmount);
    }
}
