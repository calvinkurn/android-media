package com.tokopedia.flight.search.data.api.single.request;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.flight.search.presentation.model.FlightSearchApiRequestModel;

/**
 * Created by User on 11/8/2017.
 */

public class FlightSearchSingleRequestData {
    public static final String SEARCH_SINGLE = "search_single";
    @SerializedName("type")
    private String type;
    @SerializedName("attributes")
    private Attributes attributes;

    public FlightSearchSingleRequestData(FlightSearchApiRequestModel flightSearchApiRequestModel) {
        type = SEARCH_SINGLE;
        attributes = new Attributes(flightSearchApiRequestModel);
    }

    public Attributes getAttributes() {
        return attributes;
    }

}
