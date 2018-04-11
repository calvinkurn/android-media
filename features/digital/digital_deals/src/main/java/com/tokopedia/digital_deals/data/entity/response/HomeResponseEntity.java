package com.tokopedia.digital_deals.data.entity.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeResponseEntity {

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
