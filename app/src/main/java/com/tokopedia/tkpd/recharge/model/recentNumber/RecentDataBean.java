package com.tokopedia.tkpd.recharge.model.recentNumber;

/**
 * @author Kulomady on 8/24/2016.
 */
public class RecentDataBean {
    private String type;
    private Attributes attributes;
    private Relationships relationships;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Relationships getRelationships() {
        return relationships;
    }

    public void setRelationships(Relationships relationships) {
        this.relationships = relationships;
    }

}
