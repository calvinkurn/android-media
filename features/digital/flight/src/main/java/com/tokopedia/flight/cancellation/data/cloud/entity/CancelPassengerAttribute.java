package com.tokopedia.flight.cancellation.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 22/03/18.
 */

public class CancelPassengerAttribute {
    @SerializedName("passengers")
    @Expose
    private List<Passenger> passengers;
    @SerializedName("cancellation_reason")
    @Expose
    private List<Reason> reasons;

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public List<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;
    }

}
