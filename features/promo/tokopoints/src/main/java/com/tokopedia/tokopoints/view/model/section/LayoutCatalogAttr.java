
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;

import java.util.List;

public class LayoutCatalogAttr {

    @SerializedName("catalogList")
    @Expose
    private List<CatalogsValueEntity> catalogList = null;

    public List<CatalogsValueEntity> getCatalogList() {
        return catalogList;
    }


}
