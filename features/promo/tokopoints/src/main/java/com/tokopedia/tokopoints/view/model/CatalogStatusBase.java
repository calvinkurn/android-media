package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatalogStatusBase {
    @Expose
    @SerializedName("catalogStatus")
    private List<CatalogStatusItem> catalogs;

    public List<CatalogStatusItem> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<CatalogStatusItem> catalogs) {
        this.catalogs = catalogs;
    }

    @Override
    public String toString() {
        return "CatalogStatusBase{" +
                "catalogs=" + catalogs +
                '}';
    }
}
