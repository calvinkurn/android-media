
package com.tokopedia.core.shop.model.etalasemodel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class List {

    @SerializedName("etalase_id")
    @Expose
    private Integer etalaseId;
    @SerializedName("etalase_name")
    @Expose
    private String etalaseName;
    @SerializedName("etalase_num_product")
    @Expose
    private Integer etalaseNumProduct;
    @SerializedName("etalase_total_product")
    @Expose
    private String etalaseTotalProduct;

    /**
     * 
     * @return
     *     The etalaseId
     */
    public Integer getEtalaseId() {
        return etalaseId;
    }

    /**
     * 
     * @param etalaseId
     *     The etalase_id
     */
    public void setEtalaseId(Integer etalaseId) {
        this.etalaseId = etalaseId;
    }

    /**
     * 
     * @return
     *     The etalaseName
     */
    public String getEtalaseName() {
        return etalaseName;
    }

    /**
     * 
     * @param etalaseName
     *     The etalase_name
     */
    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    /**
     * 
     * @return
     *     The etalaseNumProduct
     */
    public Integer getEtalaseNumProduct() {
        return etalaseNumProduct;
    }

    /**
     * 
     * @param etalaseNumProduct
     *     The etalase_num_product
     */
    public void setEtalaseNumProduct(Integer etalaseNumProduct) {
        this.etalaseNumProduct = etalaseNumProduct;
    }

    /**
     * 
     * @return
     *     The etalaseTotalProduct
     */
    public String getEtalaseTotalProduct() {
        return etalaseTotalProduct;
    }

    /**
     * 
     * @param etalaseTotalProduct
     *     The etalase_total_product
     */
    public void setEtalaseTotalProduct(String etalaseTotalProduct) {
        this.etalaseTotalProduct = etalaseTotalProduct;
    }

}
