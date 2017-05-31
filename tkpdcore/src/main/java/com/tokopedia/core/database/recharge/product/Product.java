
package com.tokopedia.core.database.recharge.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("relationships")
    @Expose
    private Relationships relationships;
    @SerializedName("type")
    @Expose
    private String type;

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Relationships getRelationships() {
        return relationships;
    }

    public void setRelationships(Relationships relationships) {
        this.relationships = relationships;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return attributes.getDesc();
    }
}
