package com.tokopedia.digital_deals.data.entity.response.allbrandsresponse;

import com.google.gson.annotations.SerializedName;

public class AllBrandsResponse {

    @SerializedName("data")
    private AllBrandsDataResponse data;

    public AllBrandsDataResponse getData() {
        return data;
    }

    public void setData(AllBrandsDataResponse data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
