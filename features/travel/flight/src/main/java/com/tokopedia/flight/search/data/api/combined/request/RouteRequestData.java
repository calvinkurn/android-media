package com.tokopedia.flight.search.data.api.combined.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 19/09/18.
 */
public class RouteRequestData {
    @SerializedName("departure")
    @Expose
    private String departure;

    @SerializedName("arrival")
    @Expose
    private String arrival;

    @SerializedName("date")
    @Expose
    private String date;

    public RouteRequestData(String departure, String arrival, String date) {
        this.departure = departure;
        this.arrival = arrival;
        this.date = date;
    }
}
