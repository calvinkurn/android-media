package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 5/10/17.
 */
public class ResponseRecentNumberData {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private AttributesRecentNumber attributes;
    @SerializedName("relationships")
    @Expose
    private RelationshipRecentNumber relationships;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AttributesRecentNumber getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesRecentNumber attributes) {
        this.attributes = attributes;
    }

    public RelationshipRecentNumber getRelationships() {
        return relationships;
    }

    public void setRelationships(RelationshipRecentNumber relationships) {
        this.relationships = relationships;
    }
}
