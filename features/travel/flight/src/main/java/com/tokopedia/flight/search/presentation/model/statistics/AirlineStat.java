package com.tokopedia.flight.search.presentation.model.statistics;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory;
import com.tokopedia.flight.search.presentation.model.FlightAirlineModel;

/**
 * Created by User on 11/1/2017.
 */

public class AirlineStat implements Parcelable, Visitable<BaseListCheckableTypeFactory<AirlineStat>> {
    public static final Parcelable.Creator<AirlineStat> CREATOR = new Parcelable.Creator<AirlineStat>() {
        @Override
        public AirlineStat createFromParcel(Parcel source) {
            return new AirlineStat(source);
        }

        @Override
        public AirlineStat[] newArray(int size) {
            return new AirlineStat[size];
        }
    };
    private FlightAirlineModel airlineDB;
    private int minPrice;
    private String minPriceString;

    public AirlineStat(FlightAirlineModel airlineDB, int minPrice, String minPriceString) {
        this.airlineDB = airlineDB;
        this.minPrice = minPrice;
        this.minPriceString = minPriceString;
    }

    protected AirlineStat(Parcel in) {
        this.airlineDB = in.readParcelable(FlightAirlineModel.class.getClassLoader());
        this.minPrice = in.readInt();
        this.minPriceString = in.readString();
    }

    public String getMinPriceString() {
        return minPriceString;
    }

    public void setMinPriceString(String minPriceString) {
        this.minPriceString = minPriceString;
    }

    public FlightAirlineModel getAirlineDB() {
        return airlineDB;
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
        dest.writeParcelable(this.airlineDB, flags);
        dest.writeInt(this.minPrice);
        dest.writeString(this.minPriceString);
    }

    @Override
    public int type(BaseListCheckableTypeFactory<AirlineStat> typeFactory) {
        return typeFactory.type(this);
    }
}
