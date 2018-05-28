package com.tokopedia.digital_deals.data.entity.response.categorydetailresponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class CategoryDataResponse {

    @SerializedName("grid_layout")
    JsonArray deals;

    @SerializedName("brands")
    JsonArray brands;

    @SerializedName("page")
    JsonObject page;

    public JsonArray getDeals() {
        return deals;
    }

    public void setDeals(JsonArray deals) {
        this.deals = deals;
    }

    public JsonArray getBrands() {
        return brands;
    }

    public void setBrands(JsonArray brands) {
        this.brands = brands;
    }

    public JsonObject getPage() {
        return page;
    }

    public void setPage(JsonObject page) {
        this.page = page;
    }


}
