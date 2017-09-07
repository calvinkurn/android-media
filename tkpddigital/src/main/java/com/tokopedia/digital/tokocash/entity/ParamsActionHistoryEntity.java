package com.tokopedia.digital.tokocash.entity;

import android.os.Parcel;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ParamsActionHistoryEntity {

    private String refundId;

    private String refundType;

    protected ParamsActionHistoryEntity(Parcel in) {
        refundId = in.readString();
        refundType = in.readString();
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }
}
