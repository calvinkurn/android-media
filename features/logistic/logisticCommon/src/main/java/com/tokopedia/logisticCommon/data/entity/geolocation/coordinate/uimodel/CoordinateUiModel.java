package com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.uimodel;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kris on 9/18/17. Tokopedia
 */

public class CoordinateUiModel {

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
