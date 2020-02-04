package com.tokopedia.flight.dashboard.data.cloud.entity.flightclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassAttributeEntity {
    @SerializedName("label")
    @Expose
    private String label;

    public String getLabel() {
        return label;
    }

    public FlightClassAttributeEntity(String label) {
        this.label = label;
    }
}
