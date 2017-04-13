
package com.tokopedia.core.network.entity.replacement.opportunitycategorydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OpportunityCategoryData {

    @SerializedName("shippingType")
    @Expose
    private ArrayList<ShippingType> shippingType = null;
    @SerializedName("sortingType")
    @Expose
    private ArrayList<SortingType> sortingType = null;
    @SerializedName("categoryList")
    @Expose
    private ArrayList<CategoryList> categoryList = null;

    public ArrayList<ShippingType> getShippingType() {
        return shippingType;
    }

    public void setShippingType(ArrayList<ShippingType> shippingType) {
        this.shippingType = shippingType;
    }

    public ArrayList<SortingType> getSortingType() {
        return sortingType;
    }

    public void setSortingType(ArrayList<SortingType> sortingType) {
        this.sortingType = sortingType;
    }

    public ArrayList<CategoryList> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategoryList> categoryList) {
        this.categoryList = categoryList;
    }

}
