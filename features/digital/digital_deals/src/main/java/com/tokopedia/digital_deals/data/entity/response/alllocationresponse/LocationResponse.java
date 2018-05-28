package com.tokopedia.digital_deals.data.entity.response.alllocationresponse;

import com.google.gson.annotations.SerializedName;

public class LocationResponse {

    @SerializedName("data")
    private LocationDataResponse data;

    public LocationDataResponse getData() {
        return data;
    }

    public void setData(LocationDataResponse data) {
        this.data = data;
    }
}
