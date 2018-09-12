package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 30/04/18.
 */

public class CancellationDetailsAttribute {
    @SerializedName("journey_id")
    @Expose
    private long journeyId;
    @SerializedName("passenger_id")
    @Expose
    private String passengerId;

    public long getJourneyId() {
        return journeyId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setJourneyId(long journeyId) {
        this.journeyId = journeyId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj != null && obj instanceof CancellationDetailsAttribute) {
            isEqual = (this.passengerId.equals( ((CancellationDetailsAttribute) obj).passengerId ));
        }

        if (obj != null && obj instanceof String) {
            isEqual = (this.passengerId.equals(obj));
        }

        return isEqual;
    }
}
