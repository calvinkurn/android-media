package com.tokopedia.flight.orderlist.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class FlightStopOverViewModel implements Parcelable {
    private String airportCode;
    private String cityName;

    public FlightStopOverViewModel() {
    }

    protected FlightStopOverViewModel(Parcel in) {
        airportCode = in.readString();
        cityName = in.readString();
    }

    public static final Creator<FlightStopOverViewModel> CREATOR = new Creator<FlightStopOverViewModel>() {
        @Override
        public FlightStopOverViewModel createFromParcel(Parcel in) {
            return new FlightStopOverViewModel(in);
        }

        @Override
        public FlightStopOverViewModel[] newArray(int size) {
            return new FlightStopOverViewModel[size];
        }
    };

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(airportCode);
        parcel.writeString(cityName);
    }

    @Override
    public String toString() {
        return cityName;
    }
}
