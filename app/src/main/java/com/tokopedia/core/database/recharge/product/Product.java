
package com.tokopedia.core.database.recharge.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("relationships")
    @Expose
    private Relationships relationships;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     * 
     * @return
     *     The attributes
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * 
     * @param attributes
     *     The attributes
     */
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The relationships
     */
    public Relationships getRelationships() {
        return relationships;
    }

    /**
     * 
     * @param relationships
     *     The relationships
     */
    public void setRelationships(Relationships relationships) {
        this.relationships = relationships;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return attributes.getDesc();
    }
}
