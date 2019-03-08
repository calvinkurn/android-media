package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoPointEntity {
    @Expose
    @SerializedName("resultStatus")
    private ResultStatusEntity resultStatus;

    @Expose
    @SerializedName("status")
    private TokoPointStatusEntity status;


    public ResultStatusEntity getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatusEntity resultStatus) {
        this.resultStatus = resultStatus;
    }

    public TokoPointStatusEntity getStatus() {
        return status;
    }

    public void setStatus(TokoPointStatusEntity status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TokoPointEntity{" +
                "resultStatus=" + resultStatus +
                ", status=" + status +
                '}';
    }
}
