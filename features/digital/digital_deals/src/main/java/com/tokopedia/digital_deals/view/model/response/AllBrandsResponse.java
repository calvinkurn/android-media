package com.tokopedia.digital_deals.view.model.response;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Page;

import java.util.List;

public class AllBrandsResponse {

    @SerializedName("page")
    private Page page;

    @SerializedName("brands")
    private List<Brand> brands;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
}