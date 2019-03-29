package com.tokopedia.flight.booking.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 11/13/17.
 */

public class Attribute {
    @SerializedName("flight")
    @Expose
    private FlightAttribute flightAttribute;

    public FlightAttribute getFlightAttribute() {
        return flightAttribute;
    }
}
