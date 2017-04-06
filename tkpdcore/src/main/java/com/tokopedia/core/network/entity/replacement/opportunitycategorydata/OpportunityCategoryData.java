
package com.tokopedia.core.network.entity.replacement.opportunitycategorydata;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpportunityCategoryData {

    @SerializedName("shippingType")
    @Expose
    private List<ShippingType> shippingType = null;
    @SerializedName("sortingType")
    @Expose
    private List<SortingType> sortingType = null;
    @SerializedName("categoryList")
    @Expose
    private List<CategoryList> categoryList = null;

    public List<ShippingType> getShippingType() {
        return shippingType;
    }

    public void setShippingType(List<ShippingType> shippingType) {
        this.shippingType = shippingType;
    }

    public List<SortingType> getSortingType() {
        return sortingType;
    }

    public void setSortingType(List<SortingType> sortingType) {
        this.sortingType = sortingType;
    }

    public List<CategoryList> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryList> categoryList) {
        this.categoryList = categoryList;
    }

}
