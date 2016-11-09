package com.tokopedia.discovery.catalog.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 10/18/16.
 */

public class CatalogDetailItem {
    @SerializedName("shop")
    @Expose
    private CatalogDetailItemShop catalogDetailItemShop;
    @SerializedName("products")
    @Expose
    private List<CatalogDetailItemProduct> catalogDetailItemProductList = new ArrayList<>();

    public CatalogDetailItemShop getCatalogDetailItemShop() {
        return catalogDetailItemShop;
    }

    public void setCatalogDetailItemShop(CatalogDetailItemShop catalogDetailItemShop) {
        this.catalogDetailItemShop = catalogDetailItemShop;
    }

    public List<CatalogDetailItemProduct> getCatalogDetailItemProductList() {
        return catalogDetailItemProductList;
    }

    public void setCatalogDetailItemProductList(List<CatalogDetailItemProduct> catalogDetailItemProductList) {
        this.catalogDetailItemProductList = catalogDetailItemProductList;
    }
}
