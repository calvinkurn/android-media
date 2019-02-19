package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 12/6/17.
 */

public class JourneyEntity {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("departure_id")
    @Expose
    private String departureAirportId;
    @SerializedName("departure_time")
    @Expose
    private String departureTime;
    @SerializedName("departure_airport_name")
    @Expose
    private String departureAirportName;
    @SerializedName("departure_terminal")
    @Expose
    private String departureTerminal;
    @SerializedName("departure_city_name")
    @Expose
    private String departureCityName;
    @SerializedName("arrival_id")
    @Expose
    private String arrivalAirportId;
    @SerializedName("arrival_time")
    @Expose
    private String arrivalTime;
    @SerializedName("arrival_airport_name")
    @Expose
    private String arrivalAirportName;
    @SerializedName("arrival_terminal")
    @Expose
    private String arrivalTerminal;
    @SerializedName("arrival_city_name")
    @Expose
    private String arrivalCityName;
    @SerializedName("total_transit")
    @Expose
    private int totalTransit;
    @SerializedName("total_stop")
    @Expose
    private int totalStop;
    @SerializedName("routes")
    @Expose
    private List<RouteEntity> routes;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("add_day_arrival")
    @Expose
    private String addDayArrival;

    public JourneyEntity() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDepartureAirportId() {
        return departureAirportId;
    }

    public void setDepartureAirportId(String departureAirportId) {
        this.departureAirportId = departureAirportId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalAirportId() {
        return arrivalAirportId;
    }

    public void setArrivalAirportId(String arrivalAirportId) {
        this.arrivalAirportId = arrivalAirportId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<RouteEntity> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RouteEntity> routes) {
        this.routes = routes;
    }

    public String getDuration() {
        return duration;
    }

    public String getAddDayArrival() {
        return addDayArrival;
    }

    public long getId() {
        return id;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public String getDepartureCityName() {
        return departureCityName;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public String getArrivalCityName() {
        return arrivalCityName;
    }

    public int getTotalTransit() {
        return totalTransit;
    }

    public int getTotalStop() {
        return totalStop;
    }
}
