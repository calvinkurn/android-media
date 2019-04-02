package com.tokopedia.flight.review.domain.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 1/4/18.
 */

public class FlightCheckoutData {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private FlightCheckoutAttributes attributes;

    public FlightCheckoutData() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FlightCheckoutAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(FlightCheckoutAttributes attributes) {
        this.attributes = attributes;
    }
}
