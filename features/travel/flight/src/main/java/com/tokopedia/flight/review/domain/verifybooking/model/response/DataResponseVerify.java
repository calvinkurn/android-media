
package com.tokopedia.flight.review.domain.verifybooking.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.flight.search.data.api.single.response.Meta;

public class DataResponseVerify {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private AttributesData attributesData;
    @SerializedName("meta")
    @Expose
    private Meta meta;

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

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
