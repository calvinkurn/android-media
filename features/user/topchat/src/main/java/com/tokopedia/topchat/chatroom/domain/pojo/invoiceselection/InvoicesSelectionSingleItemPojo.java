package com.tokopedia.topchat.chatroom.domain.pojo.invoiceselection;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 16/05/18.
 */
public class InvoicesSelectionSingleItemPojo {

    @SerializedName("attributes")
    private InvoiceSingleItemAttributes attributes;
    @SerializedName("type")
    private String type;
    @SerializedName("type_id")
    private int typeId;

    public InvoiceSingleItemAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(InvoiceSingleItemAttributes attributes) {
        this.attributes = attributes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
