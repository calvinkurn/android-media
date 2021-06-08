package com.tokopedia.flight.search.presentation.model.statistics;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory;
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum;

/**
 * Created by User on 11/1/2017.
 */

public class DepartureStat implements Parcelable, Visitable<BaseListCheckableTypeFactory<DepartureStat>> {
    public static final Parcelable.Creator<DepartureStat> CREATOR = new Parcelable.Creator<DepartureStat>() {
        @Override
        public DepartureStat createFromParcel(Parcel source) {
            return new DepartureStat(source);
        }

        @Override
        public DepartureStat[] newArray(int size) {
            return new DepartureStat[size];
        }
    };
    private DepartureTimeEnum departureTimeEnum;
    private int minPrice;
    private String minPriceString;

    public DepartureStat(DepartureTimeEnum departureTimeEnum, int minPrice, String minPriceString) {
        this.departureTimeEnum = departureTimeEnum;
        this.minPrice = minPrice;
        this.minPriceString = minPriceString;
    }

    protected DepartureStat(Parcel in) {
        int tmpDepartureTimeEnum = in.readInt();
        this.departureTimeEnum = tmpDepartureTimeEnum == -1 ? null : DepartureTimeEnum.values()[tmpDepartureTimeEnum];
        this.minPrice = in.readInt();
        this.minPriceString = in.readString();
    }

    public String getMinPriceString() {
        return minPriceString;
    }

    public void setMinPriceString(String minPriceString) {
        this.minPriceString = minPriceString;
    }

    public DepartureTimeEnum getDepartureTime() {
        return departureTimeEnum;
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
        dest.writeInt(this.departureTimeEnum == null ? -1 : this.departureTimeEnum.ordinal());
        dest.writeInt(this.minPrice);
        dest.writeString(this.minPriceString);
    }

    @Override
    public int type(BaseListCheckableTypeFactory<DepartureStat> typeFactory) {
        return typeFactory.type(this);
    }
}
