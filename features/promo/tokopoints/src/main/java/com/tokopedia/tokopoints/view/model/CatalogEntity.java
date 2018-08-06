package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CatalogEntity {
    @Expose
    @SerializedName("catalogs")
    private ArrayList<CatalogsValueEntity> catalogs;

    public ArrayList<CatalogsValueEntity> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(ArrayList<CatalogsValueEntity> catalogs) {
        this.catalogs = catalogs;
    }

    @Override
    public String toString() {
        return "CatalogEntity{" +
                "catalogs=" + catalogs +
                '}';
    }
}
