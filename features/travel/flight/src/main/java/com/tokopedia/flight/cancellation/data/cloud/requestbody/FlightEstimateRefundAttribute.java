package com.tokopedia.flight.cancellation.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 4/9/18.
 */

public class FlightEstimateRefundAttribute {
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("user_id")
    @Expose
    private long userId;
    @SerializedName("details")
    @Expose
    private List<FlightCancellationDetailRequestBody> details;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("reason_type")
    @Expose
    private int reasonType;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<FlightCancellationDetailRequestBody> getDetails() {
        return details;
    }

    public void setDetails(List<FlightCancellationDetailRequestBody> details) {
        this.details = details;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setReasonType(int reasonType) {
        this.reasonType = reasonType;
    }
}
