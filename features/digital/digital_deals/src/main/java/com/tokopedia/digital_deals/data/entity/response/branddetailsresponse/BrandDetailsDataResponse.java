package com.tokopedia.digital_deals.data.entity.response.branddetailsresponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class BrandDetailsDataResponse {

    @SerializedName("brand")
    private JsonObject brand;

    @SerializedName("products")
    private JsonArray deals;

    public JsonObject getBrand() {
        return brand;
    }

    public void setBrand(JsonObject brand) {
        this.brand = brand;
    }

    public JsonArray getDeals() {
        return deals;
    }

    public void setDeals(JsonArray deals) {
        this.deals = deals;
    }

    @Override
    public String toString() {
        return brand.toString()+"   "+deals.toString();
    }
}
