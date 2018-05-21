package com.tokopedia.digital_deals.data.entity.response.allbrandsresponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class AllBrandsDataResponse {

    @SerializedName("brands")
    JsonArray brands;

    @SerializedName("page")
    JsonObject page;

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
