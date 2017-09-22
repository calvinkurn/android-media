package com.tokopedia.core.geolocation.model.coordinate.viewmodel;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kris on 9/18/17. Tokopedia
 */

public class CoordinateViewModel {

    private LatLng coordinate;
    private String title;
    private String address;
    private String placeId;

    public LatLng getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
