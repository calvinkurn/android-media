
package com.tokopedia.core.network.entity.variant;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VariantOption {

    @SerializedName("pv_id")
    @Expose
    private Integer pvId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("v_id")
    @Expose
    private Integer vId;
    @SerializedName("vu_id")
    @Expose
    private Integer vuId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("option")
    @Expose
    private List<Option> option = null;

    public Integer getPvId() {
        return pvId;
    }

    public void setPvId(Integer pvId) {
        this.pvId = pvId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getVId() {
        return vId;
    }

    public void setVId(Integer vId) {
        this.vId = vId;
    }

    public Integer getVuId() {
        return vuId;
    }

    public void setVuId(Integer vuId) {
        this.vuId = vuId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<Option> getOption() {
        return option;
    }

    public void setOption(List<Option> option) {
        this.option = option;
    }

}
