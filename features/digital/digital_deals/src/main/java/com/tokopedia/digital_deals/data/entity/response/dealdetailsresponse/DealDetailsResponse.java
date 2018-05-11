package com.tokopedia.digital_deals.data.entity.response.dealdetailsresponse;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class DealDetailsResponse {

    @SerializedName("data")
    private JsonObject data;

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
