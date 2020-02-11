package com.tokopedia.digital.newcart.data.entity.requestbody.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/3/18.
 */

public class RequestBodyCancelVoucher {
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public RequestBodyCancelVoucher() {
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
