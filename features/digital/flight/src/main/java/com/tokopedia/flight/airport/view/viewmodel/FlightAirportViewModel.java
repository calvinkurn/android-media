package com.tokopedia.flight.airport.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.airport.view.adapter.FlightAirportAdapterTypeFactory;

import java.util.List;

public class FlightAirportViewModel implements Visitable<FlightAirportAdapterTypeFactory>, Parcelable {
    private String countryName;
    private String cityId;
    private String cityName;
    private String cityCode;
    private String airportName;
    private String airportCode;
    private List<String> cityAirports;

    public FlightAirportViewModel() {
    }

    protected FlightAirportViewModel(Parcel in) {
        countryName = in.readString();
        cityId = in.readString();
        cityName = in.readString();
        cityCode = in.readString();
        airportName = in.readString();
        airportCode = in.readString();
        cityAirports = in.createStringArrayList();
    }

    public static final Creator<FlightAirportViewModel> CREATOR = new Creator<FlightAirportViewModel>() {
        @Override
        public FlightAirportViewModel createFromParcel(Parcel in) {
            return new FlightAirportViewModel(in);
        }

        @Override
        public FlightAirportViewModel[] newArray(int size) {
            return new FlightAirportViewModel[size];
        }
    };

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public List<String> getCityAirports() {
        return cityAirports;
    }

    public void setCityAirports(List<String> cityAirports) {
        this.cityAirports = cityAirports;
    }

    @Override
    public int type(FlightAirportAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(countryName);
        parcel.writeString(cityId);
        parcel.writeString(cityName);
        parcel.writeString(cityCode);
        parcel.writeString(airportName);
        parcel.writeString(airportCode);
        parcel.writeStringList(cityAirports);
    }
}
