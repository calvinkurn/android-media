package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 12/6/17.
 */

public class AttributeEntity {
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("status_string")
    @Expose
    private String statusFmt;
    @SerializedName("flight")
    @Expose
    private FlightEntity flight;

    public String getCreateTime() {
        return createTime;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusFmt() {
        return statusFmt;
    }

    public FlightEntity getFlight() {
        return flight;
    }
}
