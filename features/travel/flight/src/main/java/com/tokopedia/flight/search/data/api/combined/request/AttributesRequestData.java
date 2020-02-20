package com.tokopedia.flight.search.data.api.combined.request;

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

    @SerializedName("class")
    @Expose
    private int _class;

    @SerializedName("adult")
    @Expose
    private int adult;

    @SerializedName("infant")
    @Expose
    private int infant;

    @SerializedName("child")
    @Expose
    private int child;

    public AttributesRequestData(List<RouteRequestData> routes, int _class, int adult,
                                 int infant, int child) {
        this.routes = routes;
        this._class = _class;
        this.adult = adult;
        this.infant = infant;
        this.child = child;
    }
}