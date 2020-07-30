package com.tokopedia.flight.cancellation.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/9/18.
 */

public class FlightEstimateRefundRequest {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("attributes")
    @Expose
    private FlightEstimateRefundAttribute attributes;

    public FlightEstimateRefundRequest() {
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FlightEstimateRefundAttribute getAttributes() {
        return attributes;
    }

    public void setAttributes(FlightEstimateRefundAttribute attributes) {
        this.attributes = attributes;
    }
}
