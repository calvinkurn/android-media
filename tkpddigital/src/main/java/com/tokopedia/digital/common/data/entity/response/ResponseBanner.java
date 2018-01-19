package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ResponseBanner {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private AttributesBanner attributes;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public AttributesBanner getAttributes() {
        return attributes;
    }
}
