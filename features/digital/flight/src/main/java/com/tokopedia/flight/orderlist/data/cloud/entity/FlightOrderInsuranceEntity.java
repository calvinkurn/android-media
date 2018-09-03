package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FlightOrderInsuranceEntity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;
    @SerializedName("paid_amount_numeric")
    @Expose
    private long paidAmountNumeric;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("tagline")
    @Expose
    private String tagline;

    public String getId() {
        return id;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public long getPaidAmountNumeric() {
        return paidAmountNumeric;
    }

    public String getTitle() {
        return title;
    }

    public String getTagline() {
        return tagline;
    }
}
