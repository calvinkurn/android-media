package com.tokopedia.digital_deals.data.entity.response.homeresponse;

import com.google.gson.annotations.SerializedName;

public class DealsResponse {

    @SerializedName("data")
    private DealsDataResponse data;

    public DealsDataResponse getData() {
        return data;
    }

    public void setData(DealsDataResponse data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
