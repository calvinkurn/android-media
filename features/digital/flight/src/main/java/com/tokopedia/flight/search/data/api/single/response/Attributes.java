package com.tokopedia.flight.search.data.api.single.response;

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
    @SerializedName("show_special_price_tag")
    @Expose
    private boolean showSpecialPriceTag;
    @SerializedName("fare")
    @Expose
    private Fare fare;

    public Attributes(String term, String aid, String departureAirport, String departureTime,
                      int departureTimeInt, String arrivalAirport, String arrivalTime,
                      int arrivalTimeInt, List<Route> routes, int totalTransit, int totalStop,
                      int addDayArrival, String duration, int durationMinute, String total,
                      int totalNumeric, String beforeTotal, boolean showSpecialPriceTag, Fare fare) {
        this.term = term;
        this.aid = aid;
        this.departureAirport = departureAirport;
        this.departureTime = departureTime;
        this.departureTimeInt = departureTimeInt;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = arrivalTime;
        this.arrivalTimeInt = arrivalTimeInt;
        this.routes = routes;
        this.totalTransit = totalTransit;
        this.totalStop = totalStop;
        this.addDayArrival = addDayArrival;
        this.duration = duration;
        this.durationMinute = durationMinute;
        this.total = total;
        this.totalNumeric = totalNumeric;
        this.beforeTotal = beforeTotal;
        this.showSpecialPriceTag = showSpecialPriceTag;
        this.fare = fare;
    }

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

    public boolean isShowSpecialPriceTag() { return showSpecialPriceTag; }

    public Fare getFare() {
        return fare;
    }

    public int getTotalStop() {
        return totalStop;
    }

}
