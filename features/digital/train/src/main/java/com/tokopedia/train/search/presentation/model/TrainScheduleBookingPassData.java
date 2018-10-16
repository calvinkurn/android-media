package com.tokopedia.train.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public class TrainScheduleBookingPassData implements Parcelable {

    private int adultPassenger;
    private int infantPassenger;
    private String departureScheduleId;
    private String returnScheduleId;
    private String originCity;
    private String destinationCity;

    public TrainScheduleBookingPassData() {
    }

    protected TrainScheduleBookingPassData(Parcel in) {
        adultPassenger = in.readInt();
        infantPassenger = in.readInt();
        departureScheduleId = in.readString();
        returnScheduleId = in.readString();
        originCity = in.readString();
        destinationCity = in.readString();
    }

    public static final Creator<TrainScheduleBookingPassData> CREATOR = new Creator<TrainScheduleBookingPassData>() {
        @Override
        public TrainScheduleBookingPassData createFromParcel(Parcel in) {
            return new TrainScheduleBookingPassData(in);
        }

        @Override
        public TrainScheduleBookingPassData[] newArray(int size) {
            return new TrainScheduleBookingPassData[size];
        }
    };

    public String getDepartureScheduleId() {
        return departureScheduleId;
    }

    public void setDepartureScheduleId(String departureScheduleId) {
        this.departureScheduleId = departureScheduleId;
    }

    public String getReturnScheduleId() {
        return returnScheduleId;
    }

    public void setReturnScheduleId(String returnScheduleId) {
        this.returnScheduleId = returnScheduleId;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public int getAdultPassenger() {
        return adultPassenger;
    }

    public void setAdultPassenger(int adultPassenger) {
        this.adultPassenger = adultPassenger;
    }

    public int getInfantPassenger() {
        return infantPassenger;
    }

    public void setInfantPassenger(int infantPassenger) {
        this.infantPassenger = infantPassenger;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(adultPassenger);
        parcel.writeInt(infantPassenger);
        parcel.writeString(departureScheduleId);
        parcel.writeString(returnScheduleId);
        parcel.writeString(originCity);
        parcel.writeString(destinationCity);
    }

}
