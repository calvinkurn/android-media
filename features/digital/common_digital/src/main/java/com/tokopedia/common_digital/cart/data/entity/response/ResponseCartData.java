package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class ResponseCartData {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private AttributesCart attributes;
    @SerializedName("relationships")
    @Expose
    private RelationshipsCart relationships;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public AttributesCart getAttributes() {
        return attributes;
    }

    public RelationshipsCart getRelationships() {
        return relationships;
    }
}
