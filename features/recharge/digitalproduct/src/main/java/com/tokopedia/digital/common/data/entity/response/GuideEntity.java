package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 02/07/18.
 */

public class GuideEntity {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private GuideAttributeEntity attribute;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GuideAttributeEntity getAttribute() {
        return attribute;
    }

    public void setAttribute(GuideAttributeEntity attribute) {
        this.attribute = attribute;
    }
}
