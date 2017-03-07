package com.tokopedia.digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttributesCart getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesCart attributes) {
        this.attributes = attributes;
    }

    public RelationshipsCart getRelationships() {
        return relationships;
    }

    public void setRelationships(RelationshipsCart relationships) {
        this.relationships = relationships;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
