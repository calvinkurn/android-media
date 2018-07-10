package com.tokopedia.train.station.presentation.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainStationAndCityViewModel implements Parcelable {
    private String cityName;
    private String stationCode;

    public TrainStationAndCityViewModel() {
    }

    protected TrainStationAndCityViewModel(Parcel in) {
        cityName = in.readString();
        stationCode = in.readString();
    }

    public TrainStationAndCityViewModel(String cityName, String stationCode) {
        this.cityName = cityName;
        this.stationCode = stationCode;
    }

    public static final Creator<TrainStationAndCityViewModel> CREATOR = new Creator<TrainStationAndCityViewModel>() {
        @Override
        public TrainStationAndCityViewModel createFromParcel(Parcel in) {
            return new TrainStationAndCityViewModel(in);
        }

        @Override
        public TrainStationAndCityViewModel[] newArray(int size) {
            return new TrainStationAndCityViewModel[size];
        }
    };

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cityName);
        parcel.writeString(stationCode);
    }
}
