package com.tokopedia.flight.dashboard.data.cloud.entity.flightclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassEntity {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("attributes")
    @Expose
    private FlightClassAttributeEntity attributes;

    public FlightClassEntity(int id, String type, String label) {
        this.id = id;
        this.type = type;
        this.attributes = new FlightClassAttributeEntity(label);
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public FlightClassAttributeEntity getAttributes() {
        return attributes;
    }
}
