package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogFilterOuter {
    @Expose
    @SerializedName("filter")
    private CatalogFilterBase filter;

    public CatalogFilterBase getFilter() {
        return filter;
    }

    public void setFilter(CatalogFilterBase filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "CatalogListingOuter{" +
                ", filter=" + filter +
                '}';
    }
}
