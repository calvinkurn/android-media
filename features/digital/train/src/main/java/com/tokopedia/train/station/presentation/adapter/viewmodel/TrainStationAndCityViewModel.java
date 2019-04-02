package com.tokopedia.train.station.presentation.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class TrainStationAndCityViewModel implements Parcelable {
    private String stationCode;
    private String cityName;
    private String islandName;

    public TrainStationAndCityViewModel(String stationCode, String cityName, String islandName) {
        this.stationCode = stationCode;
        this.cityName = cityName;
        this.islandName = islandName;
    }

    private TrainStationAndCityViewModel(Parcel in) {
        stationCode = in.readString();
        cityName = in.readString();
        islandName = in.readString();
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

    public String getStationCode() {
        return stationCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getIslandName() {
        return islandName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(stationCode);
        parcel.writeString(cityName);
        parcel.writeString(islandName);
    }
}
