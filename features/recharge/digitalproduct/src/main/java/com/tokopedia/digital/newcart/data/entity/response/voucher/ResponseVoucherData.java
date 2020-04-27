package com.tokopedia.digital.newcart.data.entity.response.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 3/7/17.
 */

public class ResponseVoucherData {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private AttributesVoucher attributes;
    @SerializedName("relationships")
    @Expose
    private RelationshipsVoucher relationships;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public AttributesVoucher getAttributes() {
        return attributes;
    }

    public RelationshipsVoucher getRelationships() {
        return relationships;
    }
}
