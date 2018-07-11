package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CatalogEntity {
    @Expose
    @SerializedName("catalogs")
    private ArrayList<CatalogsValueEntity> catalogs;

    @Expose
    @SerializedName("has_next")
    private boolean hasNext;

    public ArrayList<CatalogsValueEntity> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(ArrayList<CatalogsValueEntity> catalogs) {
        this.catalogs = catalogs;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public String toString() {
        return "CatalogEntity{" +
                "catalogs=" + catalogs +
                ", hasNext=" + hasNext +
                '}';
    }
}
