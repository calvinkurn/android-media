package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 30/04/18.
 */

public class CancellationEntity {
    @SerializedName("cancel_id")
    @Expose
    private int refundId;
    @SerializedName("details")
    @Expose
    private List<CancellationDetailsAttribute> details;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("estimated_refund")
    @Expose
    private String estimatedRefund;
    @SerializedName("real_refund")
    @Expose
    private String realRefund;
    @SerializedName("status")
    @Expose
    private int status;

    public int getRefundId() {
        return refundId;
    }

    public List<CancellationDetailsAttribute> getDetails() {
        return details;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getEstimatedRefund() {
        return estimatedRefund;
    }

    public String getRealRefund() {
        return realRefund;
    }

    public int getStatus() {
        return status;
    }
}
