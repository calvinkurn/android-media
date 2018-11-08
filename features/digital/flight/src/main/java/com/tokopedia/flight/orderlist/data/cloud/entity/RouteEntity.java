package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.flight.search.data.api.single.response.StopDetailEntity;

import java.util.List;

/**
 * @author by alvarisi on 12/6/17.
 */

public class RouteEntity {
    @SerializedName("departure_id")
    @Expose
    private String departureAirportCode;
    @SerializedName("departure_time")
    @Expose
    private String departureTime;
    @SerializedName("arrival_id")
    @Expose
    private String arrivalAirportCode;
    @SerializedName("arrival_time")
    @Expose
    private String arrivalTime;
    @SerializedName("airline_id")
    @Expose
    private String airlineId;
    @SerializedName("operator_airline_id")
    @Expose
    private String operatorAirlineId;
    @SerializedName("flight_number")
    @Expose
    private String flightNumber;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("layover")
    @Expose
    private String layover;
    @SerializedName("layover_minute")
    @Expose
    private int layoverMinute;
    @SerializedName("refundable")
    @Expose
    private boolean refundable;
    @SerializedName("departure_terminal")
    @Expose
    private String departureTerminal;
    @SerializedName("arrival_terminal")
    @Expose
    private String arrivalTerminal;
    @SerializedName("free_amenities")
    @Expose
    private AmenityEntity freeAmenities;
    @SerializedName("pnr")
    @Expose
    private String pnr;
    @SerializedName("stop")
    @Expose
    private int stops;
    @SerializedName("stop_detail")
    @Expose
    private List<StopDetailEntity> stopDetailEntities;

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getAirlineId() {
        return airlineId;
    }

    public String getOperatorAirlineId() {
        return operatorAirlineId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public int getLayoverMinute() {
        return layoverMinute;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public AmenityEntity getFreeAmenities() {
        return freeAmenities;
    }

    public String getPnr() {
        return pnr;
    }

    public String getDuration() {
        return duration;
    }

    public String getLayover() {
        return layover;
    }

    public int getStops() {
        return stops;
    }

    public List<StopDetailEntity> getStopDetailEntities() {
        return stopDetailEntities;
    }
}
