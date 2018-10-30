package com.tokopedia.flight.searchV2.data.api.single.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 10/26/2017.
 */

public class Attributes {
    @SerializedName("term")
    @Expose
    private String term;
    @SerializedName("aid")
    @Expose
    private String aid;
    @SerializedName("departure_airport")
    @Expose
    private String departureAirport;
    @SerializedName("departure_time")
    @Expose
    private String departureTime;
    @SerializedName("departure_time_int")
    @Expose
    private int departureTimeInt;
    @SerializedName("arrival_airport")
    @Expose
    private String arrivalAirport;
    @SerializedName("arrival_time")
    @Expose
    private String arrivalTime;
    @SerializedName("arrival_time_int")
    @Expose
    private int arrivalTimeInt;
    @SerializedName("routes")
    @Expose
    private List<Route> routes = null;
    @SerializedName("total_transit")
    @Expose
    private int totalTransit;
    @SerializedName("total_stop")
    @Expose
    private int totalStop;
    @SerializedName("add_day_arrival")
    @Expose
    private int addDayArrival;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("duration_minute")
    @Expose
    private int durationMinute;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("total_numeric")
    @Expose
    private int totalNumeric;
    @SerializedName("before_total")
    @Expose
    private String beforeTotal;
    @SerializedName("fare")
    @Expose
    private Fare fare;

    public String getTerm() {
        return term;
    }

    public String getAid() {
        return aid;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public int getDepartureTimeInt() {
        return departureTimeInt;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getArrivalTimeInt() {
        return arrivalTimeInt;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public int getTotalTransit() {
        return totalTransit;
    }

    public int getAddDayArrival() {
        return addDayArrival;
    }

    public String getDuration() {
        return duration;
    }

    public int getDurationMinute() {
        return durationMinute;
    }

    public String getTotal() {
        return total;
    }

    public int getTotalNumeric() {
        return totalNumeric;
    }

    public String getBeforeTotal() {
        return beforeTotal;
    }

    public Fare getFare() {
        return fare;
    }

    public int getTotalStop() {
        return totalStop;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setDepartureTimeInt(int departureTimeInt) {
        this.departureTimeInt = departureTimeInt;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setArrivalTimeInt(int arrivalTimeInt) {
        this.arrivalTimeInt = arrivalTimeInt;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void setTotalTransit(int totalTransit) {
        this.totalTransit = totalTransit;
    }

    public void setTotalStop(int totalStop) {
        this.totalStop = totalStop;
    }

    public void setAddDayArrival(int addDayArrival) {
        this.addDayArrival = addDayArrival;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDurationMinute(int durationMinute) {
        this.durationMinute = durationMinute;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setTotalNumeric(int totalNumeric) {
        this.totalNumeric = totalNumeric;
    }

    public void setBeforeTotal(String beforeTotal) {
        this.beforeTotal = beforeTotal;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }
}
