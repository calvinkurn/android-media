package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CatalogFilterBase {
    @SerializedName("categories")
    private ArrayList<CatalogCategory> categories;

    @SerializedName("sortType")
    private List<CatalogSortType> sortType;

    @SerializedName("pointRanges")
    private List<CatalogFilterPointRange> pointRanges;

    public ArrayList<CatalogCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CatalogCategory> categories) {
        this.categories = categories;
    }

    public List<CatalogSortType> getSortType() {
        return sortType;
    }

    public List<CatalogFilterPointRange> getPointRanges() {
        return pointRanges;
    }

    public void setPointRanges(List<CatalogFilterPointRange> pointRanges) {
        this.pointRanges = pointRanges;
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
