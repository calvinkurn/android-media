package com.tokopedia.digital.newcart.data.entity.requestbody.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;

/**
 * @author  by alvarisi on 4/3/18.
 */

public class Attributes {
    @SerializedName("identifier")
    @Expose
    private RequestBodyIdentifier identifier;

    public Attributes() {
    }

    public RequestBodyIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(RequestBodyIdentifier identifier) {
        this.identifier = identifier;
    }
}
