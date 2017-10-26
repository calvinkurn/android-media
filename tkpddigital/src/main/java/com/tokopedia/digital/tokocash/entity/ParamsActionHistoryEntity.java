package com.tokopedia.digital.tokocash.entity;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ParamsActionHistoryEntity {

    private String refund_id;

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
