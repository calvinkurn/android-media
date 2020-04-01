package com.tokopedia.digital_deals.view.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.FilterItem;
import com.tokopedia.digital_deals.view.model.Page;
import com.tokopedia.digital_deals.view.model.ProductItem;

import java.util.List;


public class SearchResponse {

    @SerializedName("grid_layout")
    @Expose
    private List<ProductItem> deals;

    private List<FilterItem> filters;

    @SerializedName("page")
    @Expose
    private Page page;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("brand_count")
    @Expose
    private int brandCount;

    @SerializedName("brands")
    @Expose
    private List<Brand> brandList;

    public List<ProductItem> getDeals() {
        return deals;
    }

    public void setDeals(List<ProductItem> deals) {
        this.deals = deals;
    }

    public List<FilterItem> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterItem> filters) {
        this.filters = filters;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBrandCount() {
        return brandCount;
    }

    public void setBrandCount(int brandCount) {
        this.brandCount = brandCount;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }
}
