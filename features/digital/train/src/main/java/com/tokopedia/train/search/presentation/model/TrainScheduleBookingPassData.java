package com.tokopedia.train.search.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public class TrainScheduleBookingPassData implements Parcelable {

    private int adultPassenger;
    private int infantPassenger;
    private TrainScheduleViewModel departureTrip;
    private TrainScheduleViewModel returnTrip;
    private String originCity;
    private String destinationCity;

    public TrainScheduleBookingPassData() {
    }

    protected TrainScheduleBookingPassData(Parcel in) {
        adultPassenger = in.readInt();
        infantPassenger = in.readInt();
        departureTrip = in.readParcelable(TrainScheduleViewModel.class.getClassLoader());
        returnTrip = in.readParcelable(TrainScheduleViewModel.class.getClassLoader());
        originCity = in.readString();
        destinationCity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(adultPassenger);
        dest.writeInt(infantPassenger);
        dest.writeParcelable(departureTrip, flags);
        dest.writeParcelable(returnTrip, flags);
        dest.writeString(originCity);
        dest.writeString(destinationCity);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public TrainScheduleViewModel getDepartureTrip() {
        return departureTrip;
    }

    public void setDepartureTrip(TrainScheduleViewModel departureTrip) {
        this.departureTrip = departureTrip;
    }

    public TrainScheduleViewModel getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(TrainScheduleViewModel returnTrip) {
        this.returnTrip = returnTrip;
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
}
