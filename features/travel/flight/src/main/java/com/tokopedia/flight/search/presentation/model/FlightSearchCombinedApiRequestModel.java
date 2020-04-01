package com.tokopedia.flight.search.presentation.model;

import java.util.List;

/**
 * Created by Rizky on 02/10/18.
 */
public class FlightSearchCombinedApiRequestModel {
    private List<FlightRouteModel> routes;
    private int adult;
    private int child;
    private int infant;
    private int _class;

    public FlightSearchCombinedApiRequestModel(List<FlightRouteModel> routes, int adult, int child,
                                               int infant, int _class) {
        this.routes = routes;
        this.adult = adult;
        this.child = child;
        this.infant = infant;
        this._class = _class;
    }

    public List<FlightRouteModel> getRoutes() {
        return routes;
    }

    public int getAdult() {
        return adult;
    }

    public int getChild() {
        return child;
    }

    public int getInfant() {
        return infant;
    }

    public int get_class() {
        return _class;
    }
}
