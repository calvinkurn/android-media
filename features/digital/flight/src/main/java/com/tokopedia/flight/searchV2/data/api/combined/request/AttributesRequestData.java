package com.tokopedia.flight.searchV2.data.api.combined.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 19/09/18.
 */
public class AttributesRequestData {

    @SerializedName("route")
    @Expose
    private List<RouteRequestData> routes;

    public AttributesRequestData(List<RouteRequestData> routes) {
        this.routes = routes;
    }

}