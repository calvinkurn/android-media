/*
 * Created By Kulomady on 11/26/16 1:14 AM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/26/16 1:14 AM
 */

package com.tokopedia.core.network.entity.discovery;

import com.tokopedia.core.var.RecyclerViewItem;

import org.parceler.Parcel;

/**
 * @author kulomady on 11/26/16.
 */
@Parcel
public class CatalogModel extends RecyclerViewItem {
    public static final int CATALOG_MODEL_TYPE = 1_234_15;

    String catalogName;
    String catalogDescription;
    String catalogImage;
    String catalogCountProduct;
    String catalogImage300;
    String catalogPrice;
    String catalogUri;
    String catalogId;

    public CatalogModel() {
        setType(CATALOG_MODEL_TYPE);
    }

    public CatalogModel(BrowseCatalogModel.Catalogs catalogs) {
        this();
        catalogName = catalogs.catalogName;
        catalogDescription = catalogs.catalogDescription;
        catalogImage = catalogs.catalogImage;
        catalogCountProduct = catalogs.catalogCountProduct;
        catalogImage300 = catalogs.catalogImage300;
        catalogPrice = catalogs.catalogPrice;
        catalogUri = catalogs.catalogUri;
        catalogId = catalogs.catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getCatalogDescription() {
        return catalogDescription;
    }

    public String getCatalogImage() {
        return catalogImage;
    }

    public String getCatalogCountProduct() {
        return catalogCountProduct;
    }

    public String getCatalogImage300() {
        return catalogImage300;
    }

    public String getCatalogPrice() {
        return catalogPrice;
    }

    public String getCatalogUri() {
        return catalogUri;
    }

    public String getCatalogId() {
        return catalogId;
    }
}
