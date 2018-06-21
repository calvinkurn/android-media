package com.tokopedia.digital_deals.data.entity.response.branddetailsresponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class BrandDetailsResponse {

    @SerializedName("brand")
    private JsonObject brand;

    @SerializedName("products")
    private JsonArray deals;

    @SerializedName("count")
    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return brand.toString()+"   "+deals.toString();
    }
}
