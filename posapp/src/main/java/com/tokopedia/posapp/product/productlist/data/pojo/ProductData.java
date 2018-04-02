package com.tokopedia.posapp.product.productlist.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author okasurya on 4/2/18.
 */

public class ProductData {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<ProductItem> data;
    @SerializedName("etalase_id")
    @Expose
    private String etalaseId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductItem> getData() {
        return data;
    }

    public void setData(List<ProductItem> data) {
        this.data = data;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }
}
