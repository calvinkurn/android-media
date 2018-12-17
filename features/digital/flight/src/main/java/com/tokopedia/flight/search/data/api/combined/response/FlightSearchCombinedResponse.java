package com.tokopedia.flight.search.data.api.combined.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 19/09/18.
 */
public class FlightSearchCombinedResponse {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("attributes")
    @Expose
    private AttributesResponse attributes;

    public FlightSearchCombinedResponse(String type, String id, AttributesResponse attributes) {
        this.type = type;
        this.id = id;
        this.attributes = attributes;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public AttributesResponse getAttributes() {
        return attributes;
    }

}