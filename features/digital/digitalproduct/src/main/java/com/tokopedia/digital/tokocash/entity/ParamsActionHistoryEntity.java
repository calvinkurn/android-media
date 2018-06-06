package com.tokopedia.digital.tokocash.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ParamsActionHistoryEntity {

    @SerializedName("refund_id")
    @Expose
    private String refund_id;
    @SerializedName("refund_type")
    @Expose
    private String refund_type;

    public ParamsActionHistoryEntity() {
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
}
