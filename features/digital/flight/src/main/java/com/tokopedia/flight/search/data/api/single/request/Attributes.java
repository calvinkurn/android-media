package com.tokopedia.flight.search.data.api.single.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.flight.search.presentation.model.FlightSearchApiRequestModel;

import java.util.List;

/**
 * Created by User on 11/8/2017.
 */

public class Attributes {
    @SerializedName("departure")
    @Expose
    private String departure;
    @SerializedName("arrival")
    @Expose
    private String arrival;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("adult")
    @Expose
    private int adult;
    @SerializedName("child")
    @Expose
    private int child;
    @SerializedName("infant")
    @Expose
    private int infant;
    @SerializedName("class")
    @Expose
    private int _class;
    @SerializedName("excluded_airlines")
    @Expose
    private List<String> excludedAirlines;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;

    public Attributes(FlightSearchApiRequestModel flightSearchApiRequestModel) {
        this.departure = flightSearchApiRequestModel.getDepAirport();
        this.arrival = flightSearchApiRequestModel.getArrAirport();
        this.date = flightSearchApiRequestModel.getDate();
        this.adult = flightSearchApiRequestModel.getAdult();
        this.child = flightSearchApiRequestModel.getChildren();
        this.infant = flightSearchApiRequestModel.getInfant();
        this._class = flightSearchApiRequestModel.getClassID();
        this.excludedAirlines = flightSearchApiRequestModel.getAirlines();
        this.ipAddress = flightSearchApiRequestModel.getIpAddress();
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDate() {
        return date;
    }
}
