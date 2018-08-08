package com.tokopedia.digital.widget.data.entity.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class StatusEntity {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private AttributesEntity attributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AttributesEntity getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesEntity attributes) {
        this.attributes = attributes;
    }
}
