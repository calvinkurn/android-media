
package com.tokopedia.core.network.entity.variant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Option {

    @SerializedName("pvo_id")
    @Expose
    private Integer pvoId;
    @SerializedName("v_id")
    @Expose
    private Integer vId;
    @SerializedName("vu_id")
    @Expose
    private Integer vuId;
    @SerializedName("vuv_id")
    @Expose
    private Integer vuvId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("hex")
    @Expose
    private String hex;
    @SerializedName("picture")
    @Expose
    private Object picture;

    public Integer getPvoId() {
        return pvoId;
    }

    public void setPvoId(Integer pvoId) {
        this.pvoId = pvoId;
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

    public Integer getVuvId() {
        return vuvId;
    }

    public void setVuvId(Integer vuvId) {
        this.vuvId = vuvId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
        this.picture = picture;
    }

}
