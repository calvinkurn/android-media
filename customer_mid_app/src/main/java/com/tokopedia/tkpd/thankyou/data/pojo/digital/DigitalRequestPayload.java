package com.tokopedia.tkpd.thankyou.data.pojo.digital;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 12/5/17.
 */

public class DigitalRequestPayload {
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;
    @SerializedName("type")
    @Expose
    private String type;

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
