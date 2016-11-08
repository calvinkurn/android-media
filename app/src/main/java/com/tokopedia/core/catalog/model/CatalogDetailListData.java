package com.tokopedia.core.catalog.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 10/18/16.
 */

public class CatalogDetailListData {
    @SerializedName("data")
    @Expose
    private List<CatalogDetailItem> catalogDetailItems = new ArrayList<>();
    @SerializedName("locations")
    @Expose
    private List<CatalogDetailListLocation> catalogDetailListLocations = new ArrayList<>();

    public List<CatalogDetailItem> getCatalogDetailItems() {
        return catalogDetailItems;
    }

    public void setCatalogDetailItems(List<CatalogDetailItem> catalogDetailItems) {
        this.catalogDetailItems = catalogDetailItems;
    }

    public List<CatalogDetailListLocation> getCatalogDetailListLocations() {
        return catalogDetailListLocations;
    }

    public void setCatalogDetailListLocations(List<CatalogDetailListLocation> catalogDetailListLocations) {
        this.catalogDetailListLocations = catalogDetailListLocations;
    }
}
