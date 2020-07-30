package com.tokopedia.flight.cancellation.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 22/03/18.
 */

public class CancelPassengerEntity {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private CancelPassengerAttribute attributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CancelPassengerAttribute getAttributes() {
        return attributes;
    }

    public void setAttributes(CancelPassengerAttribute attributes) {
        this.attributes = attributes;
    }
}
