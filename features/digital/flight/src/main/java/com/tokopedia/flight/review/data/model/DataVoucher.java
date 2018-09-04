
package com.tokopedia.flight.review.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataVoucher {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private AttributesVoucher attributesVoucher;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttributesVoucher getAttributesVoucher() {
        return attributesVoucher;
    }

    public void setAttributesVoucher(AttributesVoucher attributesVoucher) {
        this.attributesVoucher = attributesVoucher;
    }

}
