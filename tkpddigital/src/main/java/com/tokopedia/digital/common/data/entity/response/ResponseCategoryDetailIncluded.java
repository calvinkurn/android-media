package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 5/2/17.
 */

public class ResponseCategoryDetailIncluded {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private AttributesCategoryDetailIncluded attributes;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public AttributesCategoryDetailIncluded getAttributes() {
        return attributes;
    }
}
