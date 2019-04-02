package com.tokopedia.flight.search.data.db;

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
    private String airlineName;
    private String airlineShortName;
    private String airlineLogo;
    private String departureAirport;
    private String departureAirportName;
    private String departureAirportCity;
    private String arrivalAirport;
    private String arrivalAirportName;
    private String arrivalAirportCity;
    private String departureTimestamp;
    private String arrivalTimestamp;
    private String duration;
    private String infos;
    private String layover;
    private String flightNumber;
    private boolean isRefundable;
    private String amenities;
    private int stops;
    private String stopDetail;

    public FlightRouteTable(String journeyId, String airline, String airlineName, String airlineShortName,
                            String airlineLogo, String departureAirport, String departureAirportName,
                            String departureAirportCity, String arrivalAirport, String arrivalAirportName,
                            String arrivalAirportCity, String departureTimestamp, String arrivalTimestamp,
                            String duration, String infos, String layover, String flightNumber,
                            boolean isRefundable, String amenities, int stops, String stopDetail) {
        this.journeyId = journeyId;
        this.airline = airline;
        this.airlineName = airlineName;
        this.airlineShortName = airlineShortName;
        this.airlineLogo = airlineLogo;
        this.departureAirport = departureAirport;
        this.departureAirportName = departureAirportName;
        this.departureAirportCity = departureAirportCity;
        this.arrivalAirport = arrivalAirport;
        this.arrivalAirportName = arrivalAirportName;
        this.arrivalAirportCity = arrivalAirportCity;
        this.departureTimestamp = departureTimestamp;
        this.arrivalTimestamp = arrivalTimestamp;
        this.duration = duration;
        this.infos = infos;
        this.layover = layover;
        this.flightNumber = flightNumber;
        this.isRefundable = isRefundable;
        this.amenities = amenities;
        this.stops = stops;
        this.stopDetail = stopDetail;
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

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public void setAirlineShortName(String airlineShortName) {
        this.airlineShortName = airlineShortName;
    }

    public void setAirlineLogo(String airlineLogo) {
        this.airlineLogo = airlineLogo;
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

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public void setDepartureAirportCity(String departureAirportCity) {
        this.departureAirportCity = departureAirportCity;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public void setArrivalAirportCity(String arrivalAirportCity) {
        this.arrivalAirportCity = arrivalAirportCity;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public void setStopDetail(String stopDetail) {
        this.stopDetail = stopDetail;
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

    public String getAirlineName() {
        return airlineName;
    }

    public String getAirlineShortName() {
        return airlineShortName;
    }

    public String getAirlineLogo() {
        return airlineLogo;
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

    public String getInfos() {
        return infos;
    }

    public String getAmenities() {
        return amenities;
    }

    public String getStopDetail() {
        return stopDetail;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public String getDepartureAirportCity() {
        return departureAirportCity;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public String getArrivalAirportCity() {
        return arrivalAirportCity;
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
