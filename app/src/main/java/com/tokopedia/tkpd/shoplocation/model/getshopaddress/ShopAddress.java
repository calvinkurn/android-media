
package com.tokopedia.tkpd.shoplocation.model.getshopaddress;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopAddress {

    @SerializedName("list")
    @Expose
    private java.util.List<com.tokopedia.tkpd.shoplocation.model.getshopaddress.List> list = new ArrayList<com.tokopedia.tkpd.shoplocation.model.getshopaddress.List>();
    @SerializedName("is_allow")
    @Expose
    private Integer isAllow;

    /**
     * 
     * @return
     *     The list
     */
    public java.util.List<com.tokopedia.tkpd.shoplocation.model.getshopaddress.List> getList() {
        return list;
    }

    /**
     * 
     * @param list
     *     The list
     */
    public void setList(java.util.List<com.tokopedia.tkpd.shoplocation.model.getshopaddress.List> list) {
        this.list = list;
    }

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

}
