
package com.tokopedia.core.shipping.model.openshopshipping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShipmentPackage {

    @SerializedName("sp_id")
    @Expose
    private Integer spId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("desc")
    @Expose
    private String desc;

    /**
     * 
     * @return
     *     The spId
     */
    public Integer getSpId() {
        return spId;
    }

    /**
     * 
     * @param spId
     *     The sp_id
     */
    public void setSpId(Integer spId) {
        this.spId = spId;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 
     * @param desc
     *     The desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

}
