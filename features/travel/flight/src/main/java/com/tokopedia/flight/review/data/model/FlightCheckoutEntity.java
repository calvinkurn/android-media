package com.tokopedia.flight.review.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 12/20/17.
 */

public class FlightCheckoutEntity {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private FlightCheckoutAttributesEntity attributes;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public FlightCheckoutAttributesEntity getAttributes() {
        return attributes;
    }
}
