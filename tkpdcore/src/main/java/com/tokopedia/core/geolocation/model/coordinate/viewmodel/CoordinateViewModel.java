package com.tokopedia.core.geolocation.model.coordinate.viewmodel;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kris on 9/18/17. Tokopedia
 */

public class CoordinateViewModel {

    private LatLng coordinate;

    public LatLng getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }
}
