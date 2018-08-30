package com.tokopedia.flight.search.data.cloud.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchData {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public String getId() {
        return id;
    }

    public String getFlightType() {
        return type;
    }

    public Attributes getAttributes() {
        return attributes;
    }
}
