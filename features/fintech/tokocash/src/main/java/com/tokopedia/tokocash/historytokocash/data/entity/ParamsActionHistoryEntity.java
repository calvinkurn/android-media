package com.tokopedia.tokocash.historytokocash.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ParamsActionHistoryEntity {

    @SerializedName("amount")
    @Expose
    private long amount;
    @SerializedName("amount_formatted")
    @Expose
    private String amountFormatted;
    @SerializedName("refund_id")
    @Expose
    private String refund_id;
    @SerializedName("refund_type")
    @Expose
    private String refund_type;

    public ParamsActionHistoryEntity() {
    }

    public long getAmount() {
        return amount;
    }

    public String getRefundId() {
        return refund_id;
    }

    public void setRefundId(String refundId) {
        this.refund_id = refundId;
    }

    public String getRefundType() {
        return refund_type;
    }

    public void setRefundType(String refundType) {
        this.refund_type = refundType;
    }

    public String getAmountFormatted() {
        return amountFormatted;
    }
}
