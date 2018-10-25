package com.tokopedia.logisticdata.data.entity.courierlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class ShipmentPackage {

    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("sp_id")
    @Expose
    private String spId;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("name")
    @Expose
    private String name;

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

    /**
     *
     * @return
     *     The spId
     */
    public String getSpId() {
        return spId;
    }

    /**
     *
     * @param spId
     *     The sp_id
     */
    public void setSpId(String spId) {
        this.spId = spId;
    }

    /**
     *
     * @return
     *     The active
     */
    public Integer getActive() {
        return active;
    }

    /**
     *
     * @param active
     *     The active
     */
    public void setActive(Integer active) {
        this.active = active;
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


}
