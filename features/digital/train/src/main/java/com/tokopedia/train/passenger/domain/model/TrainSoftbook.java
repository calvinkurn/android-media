package com.tokopedia.train.passenger.domain.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TrainSoftbook implements Parcelable {

    private String reservationId;
    private String tokpedBookCode;
    private String expiryTimestamp;
    private List<TrainTrip> departureTrips;
    private List<TrainTrip> returnTrips;

    public TrainSoftbook(String reservationId, String tokpedBookCode, String expiryTimestamp, List<TrainTrip> departureTrips, List<TrainTrip> returnTrips) {
        this.reservationId = reservationId;
        this.tokpedBookCode = tokpedBookCode;
        this.expiryTimestamp = expiryTimestamp;
        this.departureTrips = departureTrips;
        this.returnTrips = returnTrips;
    }

    protected TrainSoftbook(Parcel in) {
        reservationId = in.readString();
        tokpedBookCode = in.readString();
        expiryTimestamp = in.readString();
        departureTrips = in.createTypedArrayList(TrainTrip.CREATOR);
        returnTrips = in.createTypedArrayList(TrainTrip.CREATOR);
    }

    public static final Creator<TrainSoftbook> CREATOR = new Creator<TrainSoftbook>() {
        @Override
        public TrainSoftbook createFromParcel(Parcel in) {
            return new TrainSoftbook(in);
        }

        @Override
        public TrainSoftbook[] newArray(int size) {
            return new TrainSoftbook[size];
        }
    };

    public String getReservationId() {
        return reservationId;
    }

    public String getTokpedBookCode() {
        return tokpedBookCode;
    }

    public String getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public List<TrainTrip> getDepartureTrips() {
        return departureTrips;
    }

    public List<TrainTrip> getReturnTrips() {
        return returnTrips;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reservationId);
        dest.writeString(tokpedBookCode);
        dest.writeString(expiryTimestamp);
        dest.writeTypedList(departureTrips);
        dest.writeTypedList(returnTrips);
    }

}
