
package com.tokopedia.core.network.entity.replacement.opportunitycategorydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Deprecated
public class OpportunityCategoryData {

    @SerializedName("sort")
    @Expose
    private ArrayList<SortData> sort = null;
    @SerializedName("filter")
    @Expose
    private ArrayList<FilterData> filter = null;

    public ArrayList<SortData> getSort() {
        return sort;
    }

    public void setSort(ArrayList<SortData> sort) {
        this.sort = sort;
    }

    public ArrayList<FilterData> getFilter() {
        return filter;
    }

    public void setFilter(ArrayList<FilterData> filter) {
        this.filter = filter;
    }
}
