package com.tokopedia.digital_deals.view.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.view.model.Location;

import java.util.List;

public class LocationResponse {

    @SerializedName("location")
    @Expose
    private List<Location> locations;

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
