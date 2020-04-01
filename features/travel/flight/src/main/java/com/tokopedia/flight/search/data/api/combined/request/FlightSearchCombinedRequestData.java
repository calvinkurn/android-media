package com.tokopedia.flight.search.data.api.combined.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 19/09/18.
 */
public class FlightSearchCombinedRequestData {

    public static final String SEARCH_COMBINED = "search_combined";

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("attributes")
    @Expose
    private AttributesRequestData attributes;

    public FlightSearchCombinedRequestData(AttributesRequestData attributes) {
        type = SEARCH_COMBINED;
        this.attributes = attributes;
    }

    public AttributesRequestData getAttributes() {
        return attributes;
    }

}
