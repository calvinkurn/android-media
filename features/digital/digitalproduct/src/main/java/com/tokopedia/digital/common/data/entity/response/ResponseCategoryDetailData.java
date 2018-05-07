package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class ResponseCategoryDetailData {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private AttributesCategoryDetail attributes;
    @SerializedName("relationships")
    @Expose
    private RelationshipsCategoryDetail relationships;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public AttributesCategoryDetail getAttributes() {
        return attributes;
    }

    public RelationshipsCategoryDetail getRelationships() {
        return relationships;
    }
}
