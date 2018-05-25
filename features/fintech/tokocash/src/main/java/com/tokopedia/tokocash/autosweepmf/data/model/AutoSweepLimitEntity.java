package com.tokopedia.tokocash.autosweepmf.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutoSweepLimitEntity {
    @Expose
    @SerializedName("status")
    private boolean status;

    @Expose
    @SerializedName("result")
    private ResultEntity result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AutoSweepLimitEntity{" +
                "status=" + status +
                ", result=" + result +
                '}';
    }
}
