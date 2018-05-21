package com.tokopedia.digital.product.data.entity.requestbody.inquirybalance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 21/05/18.
 */
public class RequestBodyInquiryBalance {

    @SerializedName("smartcard_inquiry")
    @Expose
    private String smartcardInquiry;

    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public RequestBodyInquiryBalance(String smartcardInquiry, Attributes attributes) {
        this.smartcardInquiry = smartcardInquiry;
        this.attributes = attributes;
    }
}
