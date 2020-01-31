
package com.tokopedia.flight.review.domain.verifybooking.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private AttributesData attributesData;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AttributesData getAttributesData() {
        return attributesData;
    }

    public void setAttributesData(AttributesData attributesData) {
        this.attributesData = attributesData;
    }

}
