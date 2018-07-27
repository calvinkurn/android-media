package com.tokopedia.flight.cancellation.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 11/04/18.
 */

public class FlightCancellationRequestBody {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private FlightCancellationRequestAttribute attributes;

    public FlightCancellationRequestBody() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FlightCancellationRequestAttribute getAttributes() {
        return attributes;
    }

    public void setAttributes(FlightCancellationRequestAttribute attributes) {
        this.attributes = attributes;
    }
}
