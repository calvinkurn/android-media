
package com.tokopedia.logisticdata.data.entity.geolocation.coordinate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Northeast_ {

    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lng")
    @Expose
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}
