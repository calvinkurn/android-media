package com.tokopedia.digital.widget.data.entity.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class ProductEntity {

    @SerializedName("attributes")
    @Expose
    private AttributesEntity attributes;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("relationships")
    @Expose
    private RelationshipEntity relationships;
    @SerializedName("type")
    @Expose
    private String type;

    public AttributesEntity getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesEntity attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RelationshipEntity getRelationships() {
        return relationships;
    }

    public void setRelationships(RelationshipEntity relationships) {
        this.relationships = relationships;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
