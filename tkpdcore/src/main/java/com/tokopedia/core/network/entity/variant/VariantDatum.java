
package com.tokopedia.core.network.entity.variant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VariantDatum {

    @SerializedName("pvd_id")
    @Expose
    private Integer pvdId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("stock")
    @Expose
    private Integer stock;
    @SerializedName("PvoListString")
    @Expose
    private String pvoListString;
    @SerializedName("v_code")
    @Expose
    private String vCode;

    public Integer getPvdId() {
        return pvdId;
    }

    public void setPvdId(Integer pvdId) {
        this.pvdId = pvdId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getPvoListString() {
        return pvoListString;
    }

    public void setPvoListString(String pvoListString) {
        this.pvoListString = pvoListString;
    }

    public String getVCode() {
        return vCode;
    }

    public void setVCode(String vCode) {
        this.vCode = vCode;
    }

}
