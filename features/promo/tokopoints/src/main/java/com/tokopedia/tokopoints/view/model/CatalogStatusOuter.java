package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogStatusOuter {
    @Expose
    @SerializedName("tokopointsCatalogStatus")
    CatalogStatusBase catalogStatus;

    public CatalogStatusBase getCatalogStatus() {
        return catalogStatus;
    }

    public void setCatalogStatus(CatalogStatusBase catalogStatus) {
        this.catalogStatus = catalogStatus;
    }

    @Override
    public String toString() {
        return "CatalogStatusOuter{" +
                "catalogStatus=" + catalogStatus +
                '}';
    }
}
