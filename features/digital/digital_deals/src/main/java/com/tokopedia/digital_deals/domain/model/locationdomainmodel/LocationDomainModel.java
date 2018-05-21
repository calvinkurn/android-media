package com.tokopedia.digital_deals.domain.model.locationdomainmodel;

import java.util.List;

public class LocationDomainModel {

    private List<LocationItemDomain> locations;

    public List<LocationItemDomain> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationItemDomain> locations) {
        this.locations = locations;
    }
}
