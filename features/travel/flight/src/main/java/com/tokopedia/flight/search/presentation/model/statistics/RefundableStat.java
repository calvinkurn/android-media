package com.tokopedia.flight.search.presentation.model.statistics;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory;
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum;

public class RefundableStat implements Parcelable, Visitable<BaseListCheckableTypeFactory<RefundableStat>> {
    public static final Parcelable.Creator<RefundableStat> CREATOR = new Parcelable.Creator<RefundableStat>() {
        @Override
        public RefundableStat createFromParcel(Parcel source) {
            return new RefundableStat(source);
        }

        @Override
        public RefundableStat[] newArray(int size) {
            return new RefundableStat[size];
        }
    };
    private RefundableEnum refundableEnum;
    private int minPrice;
    private String minPriceString;

    public RefundableStat(RefundableEnum refundableEnum, int minPrice, String minPriceString) {
        this.refundableEnum = refundableEnum;
        this.minPrice = minPrice;
        this.minPriceString = minPriceString;
    }

    protected RefundableStat(Parcel in) {
        int tmpRefundableEnum = in.readInt();
        this.refundableEnum = tmpRefundableEnum == -1 ? null : RefundableEnum.values()[tmpRefundableEnum];
        this.minPrice = in.readInt();
        this.minPriceString = in.readString();
    }

    public String getMinPriceString() {
        return minPriceString;
    }

    public void setMinPriceString(String minPriceString) {
        this.minPriceString = minPriceString;
    }

    public RefundableEnum getRefundableEnum() {
        return refundableEnum;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.refundableEnum == null ? -1 : this.refundableEnum.ordinal());
        dest.writeInt(this.minPrice);
        dest.writeString(this.minPriceString);
    }

    @Override
    public int type(BaseListCheckableTypeFactory<RefundableStat> typeFactory) {
        return typeFactory.type(this);
    }
}
