package com.tokopedia.flight.search.data.api.single.response;

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

    public FlightSearchData(String type, String id, Attributes attributes) {
        this.type = type;
        this.id = id;
        this.attributes = attributes;
    }

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
