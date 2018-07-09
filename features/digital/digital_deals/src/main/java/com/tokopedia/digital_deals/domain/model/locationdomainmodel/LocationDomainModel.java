package com.tokopedia.digital_deals.domain.model.locationdomainmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationDomainModel {

    @SerializedName("location")
    @Expose
    private List<LocationItemDomain> locations;

    public List<LocationItemDomain> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationItemDomain> locations) {
        this.locations = locations;
    }
}
