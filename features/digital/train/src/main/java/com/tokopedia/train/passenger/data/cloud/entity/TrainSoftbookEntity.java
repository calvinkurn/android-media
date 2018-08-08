package com.tokopedia.train.passenger.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainSoftbookEntity {

    @SerializedName("reservationId")
    @Expose
    private String reservationId;
    @SerializedName("reservationCode")
    @Expose
    private String tokpedBookCode;
    @SerializedName("expiryTimestamp")
    @Expose
    private String expiryTimestamp;

    @SerializedName("departureTrip")
    @Expose
    private List<TrainTripEntity> departureTrips;
    @SerializedName("returnTrip")
    @Expose
    private List<TrainTripEntity> returnTrips;

    public String getReservationId() {
        return reservationId;
    }

    public String getReservationCode() {
        return tokpedBookCode;
    }

    public String getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public List<TrainTripEntity> getDepartureTrips() {
        return departureTrips;
    }

    public List<TrainTripEntity> getReturnTrips() {
        return returnTrips;
    }
}
