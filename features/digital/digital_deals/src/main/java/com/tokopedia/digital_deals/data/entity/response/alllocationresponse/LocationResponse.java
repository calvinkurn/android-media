package com.tokopedia.digital_deals.data.entity.response.alllocationresponse;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class LocationResponse {

    @SerializedName("location")
    private JsonArray locations;

    public JsonArray getLocations() {
        return locations;
    }

    public void setLocations(JsonArray locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return locations.toString();
    }
}
