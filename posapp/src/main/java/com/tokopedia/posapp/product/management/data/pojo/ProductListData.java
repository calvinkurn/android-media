package com.tokopedia.posapp.product.management.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.posapp.base.data.pojo.PosData;

import java.util.List;

/**
 * @author okasurya on 4/2/18.
 * will be renamed as product management
 */

public class ProductListData extends PosData<List<ProductItem>> {
    @SerializedName("etalase_id")
    @Expose
    private String etalaseId;

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }
}
