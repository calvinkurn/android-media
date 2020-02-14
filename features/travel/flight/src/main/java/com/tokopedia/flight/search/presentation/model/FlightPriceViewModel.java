package com.tokopedia.flight.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightPriceViewModel implements Parcelable{
    private FlightFareViewModel departurePrice;
    private FlightFareViewModel returnPrice;
    private String comboKey;

    public FlightPriceViewModel() {
    }

    public FlightPriceViewModel(FlightFareViewModel departurePrice, FlightFareViewModel returnPrice, String comboKey) {
        this.departurePrice = departurePrice;
        this.returnPrice = returnPrice;
        this.comboKey = comboKey;
    }

    protected FlightPriceViewModel(Parcel in) {
        departurePrice = in.readParcelable(FlightFareViewModel.class.getClassLoader());
        returnPrice = in.readParcelable(FlightFareViewModel.class.getClassLoader());
        comboKey = in.readString();
    }

    public static final Creator<FlightPriceViewModel> CREATOR = new Creator<FlightPriceViewModel>() {
        @Override
        public FlightPriceViewModel createFromParcel(Parcel in) {
            return new FlightPriceViewModel(in);
        }

        @Override
        public FlightPriceViewModel[] newArray(int size) {
            return new FlightPriceViewModel[size];
        }
    };

    public void setDeparturePrice(FlightFareViewModel departurePrice) {
        this.departurePrice = departurePrice;
    }

    public void setReturnPrice(FlightFareViewModel returnPrice) {
        this.returnPrice = returnPrice;
    }

    public void setComboKey(String comboKey) {
        this.comboKey = comboKey;
    }

    public FlightFareViewModel getDeparturePrice() {
        return departurePrice;
    }

    public FlightFareViewModel getReturnPrice() {
        return returnPrice;
    }

    public String getComboKey() {
        return comboKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(departurePrice, i);
        parcel.writeParcelable(returnPrice, i);
        parcel.writeString(comboKey);
    }
}
