package com.tokopedia.digital_deals.data.entity.response.categorydetailresponse;

import com.google.gson.annotations.SerializedName;

public class CategoryResponse {

    @SerializedName("data")
    private CategoryDataResponse data;

    public CategoryDataResponse getData() {
        return data;
    }

    public void setData(CategoryDataResponse data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
