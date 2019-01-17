package com.tokopedia.topchat.chatlist.domain.pojo.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 27/03/18.
 */

public class AttachmentInvoice {
    @SerializedName("attributes")
    @Expose
    private AttachmentInvoiceAttributes attributes;

    @SerializedName("type")
    @Expose
    String typeString;

    @SerializedName("type_id")
    @Expose
    int type;

    public AttachmentInvoice() {
    }

    public AttachmentInvoice(AttachmentInvoiceAttributes attributes, String typeString, int type) {
        this.attributes = attributes;
        this.typeString = typeString;
        this.type = type;
    }

    public AttachmentInvoiceAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(AttachmentInvoiceAttributes attributes) {
        this.attributes = attributes;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
