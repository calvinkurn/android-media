package com.tokopedia.flight.cancellation.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/9/18.
 */

public class EstimateRefundResultEntity {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private EstimateRefundAttributeEntity attribute;

    public EstimateRefundResultEntity() {
    }

    public String getType() {
        return type;
    }

    public EstimateRefundAttributeEntity getAttribute() {
        return attribute;
    }
}
