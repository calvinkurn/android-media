package com.tokopedia.digital_deals.data.entity.response.homeresponse;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class DealsDataResponse {

    @SerializedName("home")
    private HomeResponse home;

    @SerializedName("brands")
    private JsonArray brands;

    public HomeResponse getHome() {
        return home;
    }

    public void setHome(HomeResponse home) {
        this.home = home;
    }

    public JsonArray getBrands() {
        return brands;
    }

    public void setBrands(JsonArray brands) {
        this.brands = brands;
    }

    @Override
    public String toString() {
        return home.toString();
    }
}
