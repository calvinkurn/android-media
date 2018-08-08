package com.tokopedia.digital.widget.data.entity.recentnumber;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class RecentNumberEntity {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private AttributesEntity attributes;
    @SerializedName("relationships")
    @Expose
    private RelationshipEntity relationships;

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

    public RelationshipEntity getRelationships() {
        return relationships;
    }

    public void setRelationships(RelationshipEntity relationships) {
        this.relationships = relationships;
    }
}
