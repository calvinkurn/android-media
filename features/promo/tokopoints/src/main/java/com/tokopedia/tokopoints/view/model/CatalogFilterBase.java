package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CatalogFilterBase {
    @Expose
    @SerializedName("categories")
    private ArrayList<CatalogCategory> categories;

    @Expose
    @SerializedName("sortType")
    private List<CatalogSortType> sortType;

    public ArrayList<CatalogCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CatalogCategory> categories) {
        this.categories = categories;
    }

    public List<CatalogSortType> getSortType() {
        return sortType;
    }

    public void setSortType(List<CatalogSortType> sortType) {
        this.sortType = sortType;
    }

    @Override
    public String toString() {
        return "CatalogFilterBase{" +
                "categories=" + categories +
                ", sortType=" + sortType +
                '}';
    }
}
