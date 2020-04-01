package com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardinquiry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 21/05/18.
 */
public class RequestBodySmartcardInquiry {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public RequestBodySmartcardInquiry(String type, Attributes attributes) {
        this.type = type;
        this.attributes = attributes;
    }

}
