
package com.tokopedia.tokopoints.view.model.section;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;

public class LayoutCatalogAttr {

    @SerializedName("catalogList")
    @Expose
    private List<CatalogsValueEntity> catalogList = null;

    public List<CatalogsValueEntity> getCatalogList() {
        return catalogList;
    }

    public void setCatalogList(List<CatalogsValueEntity> catalogList) {
        this.catalogList = catalogList;
    }

}
