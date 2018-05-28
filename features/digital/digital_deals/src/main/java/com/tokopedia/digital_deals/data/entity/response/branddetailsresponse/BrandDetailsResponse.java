package com.tokopedia.digital_deals.data.entity.response.branddetailsresponse;

import com.google.gson.annotations.SerializedName;

public class BrandDetailsResponse {

    @SerializedName("data")
    private BrandDetailsDataResponse data;

    public BrandDetailsDataResponse getData() {
        return data;
    }

    public void setData(BrandDetailsDataResponse data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
