
package com.tokopedia.core.shop.model.etalasemodel;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("is_allow")
    @Expose
    private Integer isAllow;
    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("list")
    @Expose
    private java.util.List<com.tokopedia.core.shop.model.etalasemodel.List> list = new ArrayList<com.tokopedia.core.shop.model.etalasemodel.List>();

    /**
     * 
     * @return
     *     The isAllow
     */
    public Integer getIsAllow() {
        return isAllow;
    }

    /**
     * 
     * @param isAllow
     *     The is_allow
     */
    public void setIsAllow(Integer isAllow) {
        this.isAllow = isAllow;
    }

    /**
     * 
     * @return
     *     The paging
     */
    public Paging getPaging() {
        return paging;
    }

    /**
     * 
     * @param paging
     *     The paging
     */
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    /**
     * 
     * @return
     *     The list
     */
    public java.util.List<com.tokopedia.core.shop.model.etalasemodel.List> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<com.tokopedia.core.shop.model.etalasemodel.List> list) {
        this.list = list;
    }

}
