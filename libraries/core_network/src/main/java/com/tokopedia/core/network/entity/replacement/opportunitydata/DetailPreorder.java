
package com.tokopedia.core.network.entity.replacement.opportunitydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class DetailPreorder {

    @SerializedName("preorder_status")
    @Expose
    private int preorderStatus;
    @SerializedName("preorder_process_time_type")
    @Expose
    private Object preorderProcessTimeType;
    @SerializedName("preorder_process_time_type_string")
    @Expose
    private Object preorderProcessTimeTypeString;
    @SerializedName("preorder_process_time")
    @Expose
    private Object preorderProcessTime;

    public int getPreorderStatus() {
        return preorderStatus;
    }

    public void setPreorderStatus(int preorderStatus) {
        this.preorderStatus = preorderStatus;
    }

    public Object getPreorderProcessTimeType() {
        return preorderProcessTimeType;
    }

    public void setPreorderProcessTimeType(Object preorderProcessTimeType) {
        this.preorderProcessTimeType = preorderProcessTimeType;
    }

    public Object getPreorderProcessTimeTypeString() {
        return preorderProcessTimeTypeString;
    }

    public void setPreorderProcessTimeTypeString(Object preorderProcessTimeTypeString) {
        this.preorderProcessTimeTypeString = preorderProcessTimeTypeString;
    }

    public Object getPreorderProcessTime() {
        return preorderProcessTime;
    }

    public void setPreorderProcessTime(Object preorderProcessTime) {
        this.preorderProcessTime = preorderProcessTime;
    }

}
