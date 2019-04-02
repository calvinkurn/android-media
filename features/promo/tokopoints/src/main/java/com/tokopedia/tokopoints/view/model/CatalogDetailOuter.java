package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.SerializedName;

public class CatalogDetailOuter {

    @SerializedName("detail")
    private CatalogsValueEntity detail;

    public CatalogsValueEntity getDetail() {
        return detail;
    }

    public void setDetail(CatalogsValueEntity detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "CatalogDetailOuter{" +
                "detail=" + detail +
                '}';
    }
}
