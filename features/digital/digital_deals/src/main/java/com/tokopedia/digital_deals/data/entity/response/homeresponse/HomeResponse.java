package com.tokopedia.digital_deals.data.entity.response.homeresponse;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeResponse {

    @SerializedName("layout")
    private JsonObject layout;

    public JsonObject getLayout() {
        return layout;
    }

    public void setLayout(JsonObject layout) {
        this.layout = layout;
    }

    @Override
    public String toString() {
        return layout.toString();
    }
}
