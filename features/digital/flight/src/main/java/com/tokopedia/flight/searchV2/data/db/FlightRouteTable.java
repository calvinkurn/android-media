package com.tokopedia.flight.searchV2.data.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Rizky on 01/10/18.
 */
@Entity
public class FlightRouteTable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String journeyId;
    private String airline;
    private String departureAirport;
    private String arrivalAirport;
    private String departureTimestamp;
    private String arrivalTimestamp;
    private String duration;
    private String layover;
    private String flightNumber;
    private boolean isRefundable;
    private int stops;

    public FlightRouteTable() {}

    public FlightRouteTable(String journeyId, String airline, String departureAirport, String arrivalAirport,
                            String departureTimestamp, String arrivalTimestamp, String duration, String layover,
                            String flightNumber, boolean isRefundable, int stops) {
        this.journeyId = journeyId;
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTimestamp = departureTimestamp;
        this.arrivalTimestamp = arrivalTimestamp;
        this.duration = duration;
        this.layover = layover;
        this.flightNumber = flightNumber;
        this.isRefundable = isRefundable;
        this.stops = stops;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public void setDepartureTimestamp(String departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public void setArrivalTimestamp(String arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setLayover(String layover) {
        this.layover = layover;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public int getId() {
        return id;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public String getAirline() {
        return airline;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public String getDuration() {
        return duration;
    }

    public String getLayover() {
        return layover;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public int getStops() {
        return stops;
    }
}
