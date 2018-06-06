package com.tokopedia.digital.widget.data.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public class ResponseFavoriteNumber {
    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String id;

    @SerializedName("attributes")
    private Attributes attributes;

    @SerializedName("relationships")
    private Relationships relationships;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public Relationships getRelationships() {
        return relationships;
    }
}
