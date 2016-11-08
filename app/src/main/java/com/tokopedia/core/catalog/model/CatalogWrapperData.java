package com.tokopedia.core.catalog.model;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogWrapperData {
    private TKPDMapParam<String, String> param;
    private String catalogId;
    private CatalogDetailData catalogDetailData;

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public CatalogDetailData getCatalogDetailData() {
        return catalogDetailData;
    }

    public void setCatalogDetailData(CatalogDetailData catalogDetailData) {
        this.catalogDetailData = catalogDetailData;
    }

    public TKPDMapParam<String, String> getParam() {
        return param;
    }

    public void setParam(TKPDMapParam<String, String> param) {
        this.param = param;
        this.catalogId = param.get("catalog_id");
    }
}
