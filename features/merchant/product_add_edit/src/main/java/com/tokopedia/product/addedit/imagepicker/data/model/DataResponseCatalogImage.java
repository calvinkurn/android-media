
package com.tokopedia.product.addedit.imagepicker.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataResponseCatalogImage {

    @SerializedName("catalog")
    @Expose
    private Catalog catalog;

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

}
