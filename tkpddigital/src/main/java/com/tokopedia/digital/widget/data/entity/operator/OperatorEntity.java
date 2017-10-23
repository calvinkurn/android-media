package com.tokopedia.digital.widget.data.entity.operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class OperatorEntity {

    @SerializedName("attributes")
    @Expose
    private AttributesEntity attributes;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("type")
    @Expose
    private String type;

    public AttributesEntity getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesEntity attributes) {
        this.attributes = attributes;
    }

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
}
