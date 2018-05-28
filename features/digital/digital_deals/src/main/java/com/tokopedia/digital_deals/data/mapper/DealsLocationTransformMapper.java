package com.tokopedia.digital_deals.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.data.entity.response.alllocationresponse.LocationResponse;
import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.digital_deals.domain.model.locationdomainmodel.LocationDomainModel;
import com.tokopedia.digital_deals.domain.model.locationdomainmodel.LocationItemDomain;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class DealsLocationTransformMapper implements Func1<LocationResponse, LocationDomainModel>{

    @Override
    public LocationDomainModel call(LocationResponse locationResponse) {

        LocationDomainModel locationDomain= new LocationDomainModel();
        JsonArray locations=locationResponse.getData().getLocations();
        List<LocationItemDomain> locationItemDomains = new ArrayList<>();
        LocationItemDomain locationItem;
        if (locations != null) {
            for (JsonElement entry : locations) {
                locationItem = new Gson().fromJson(entry.getAsJsonObject(), LocationItemDomain.class);
                locationItemDomains.add(locationItem);
            }
        }
        locationDomain.setLocations(locationItemDomains);
        return locationDomain;
    }
}
