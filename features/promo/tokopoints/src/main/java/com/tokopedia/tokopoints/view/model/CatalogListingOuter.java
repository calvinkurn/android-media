package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogListingOuter {
    @Expose
    @SerializedName("catalog")
    private CatalogEntity catalogs;

    public CatalogEntity getCatalog() {
        return catalogs;
    }

    public void setCatalogs(CatalogEntity catalogs) {
        this.catalogs = catalogs;
    }

    @Override
    public String toString() {
        return "CatalogListingOuter{" +
                "catalogs=" + catalogs +
                '}';
    }
}
