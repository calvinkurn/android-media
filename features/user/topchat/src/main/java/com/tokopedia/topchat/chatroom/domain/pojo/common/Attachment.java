
package com.tokopedia.topchat.chatroom.domain.pojo.common;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private JsonObject attributes;
    @SerializedName("fallback_attachment")
    @Expose
    private FallbackAttachment fallbackAttachment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonObject getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonObject attributes) {
        this.attributes = attributes;
    }

    public FallbackAttachment getFallbackAttachment() {
        return fallbackAttachment;
    }

    public void setFallbackAttachment(FallbackAttachment fallbackAttachment) {
        this.fallbackAttachment = fallbackAttachment;
    }
}
