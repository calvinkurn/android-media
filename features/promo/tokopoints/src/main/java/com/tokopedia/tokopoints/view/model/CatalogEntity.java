package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CatalogEntity {
    @Expose
    @SerializedName("catalogs")
    private ArrayList<CatalogsValueEntity> catalogs;

    @SerializedName("pageInfo")
    private TokopointPaging paging;

    public ArrayList<CatalogsValueEntity> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(ArrayList<CatalogsValueEntity> catalogs) {
        this.catalogs = catalogs;
    }

    public TokopointPaging getPaging() {
        return paging;
    }

    public void setPaging(TokopointPaging paging) {
        this.paging = paging;
    }

    @Override
    public String toString() {
        return "CatalogEntity{" +
                "catalogs=" + catalogs +
                '}';
    }
}
