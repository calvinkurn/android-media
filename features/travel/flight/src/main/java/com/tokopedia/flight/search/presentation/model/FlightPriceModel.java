package com.tokopedia.flight.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightPriceModel implements Parcelable{
    private FlightFareModel departurePrice;
    private FlightFareModel returnPrice;
    private String comboKey;

    public FlightPriceModel() {
    }

    public FlightPriceModel(FlightFareModel departurePrice, FlightFareModel returnPrice, String comboKey) {
        this.departurePrice = departurePrice;
        this.returnPrice = returnPrice;
        this.comboKey = comboKey;
    }

    protected FlightPriceModel(Parcel in) {
        departurePrice = in.readParcelable(FlightFareModel.class.getClassLoader());
        returnPrice = in.readParcelable(FlightFareModel.class.getClassLoader());
        comboKey = in.readString();
    }

    public static final Creator<FlightPriceModel> CREATOR = new Creator<FlightPriceModel>() {
        @Override
        public FlightPriceModel createFromParcel(Parcel in) {
            return new FlightPriceModel(in);
        }

        @Override
        public FlightPriceModel[] newArray(int size) {
            return new FlightPriceModel[size];
        }
    };

    public void setDeparturePrice(FlightFareModel departurePrice) {
        this.departurePrice = departurePrice;
    }

    public void setReturnPrice(FlightFareModel returnPrice) {
        this.returnPrice = returnPrice;
    }

    public void setComboKey(String comboKey) {
        this.comboKey = comboKey;
    }

    public FlightFareModel getDeparturePrice() {
        return departurePrice;
    }

    public FlightFareModel getReturnPrice() {
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
