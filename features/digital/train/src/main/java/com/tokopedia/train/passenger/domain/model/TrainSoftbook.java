package com.tokopedia.train.passenger.domain.model;


import java.util.List;

public class TrainSoftbook {
    private String reservationId;
    private String tokpedBookCode;
    private String expiryTimestamp;
    private List<TrainTrip> departureTrips;
    private List<TrainTrip> returnTrips;

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
}
