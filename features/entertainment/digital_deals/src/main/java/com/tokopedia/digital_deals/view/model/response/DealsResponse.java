package com.tokopedia.digital_deals.view.model.response;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.FilterItem;
import com.tokopedia.digital_deals.view.model.response.HomeResponse;

import java.util.List;

public class DealsResponse {

    @SerializedName("home")
    private HomeResponse home;

    @SerializedName("brands")
    private List<Brand> brands;

    @SerializedName("filters")
    private List<FilterItem> filters;

    private transient List<CategoryItem> categoryItems;

    public HomeResponse getHome() {
        return home;
    }

    public void setHome(HomeResponse home) {
        this.home = home;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<FilterItem> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterItem> filters) {
        this.filters = filters;
    }

    public List<CategoryItem> getCategoryItems() {
        return categoryItems;
    }

    public void setCategoryItems(List<CategoryItem> categoryItems) {
        this.categoryItems = categoryItems;
    }

    @Override
    public String toString() {
        return home.toString()+" "+brands.toString();
    }
}
