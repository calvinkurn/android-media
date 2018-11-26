package com.tokopedia.user_identification_common.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 15/11/18.
 */
public class KycStatusDetailPojo {

    @Expose
    @SerializedName("IsSuccess")
    private int isSuccess;

    @Expose
    @SerializedName("Status")
    private int status;

    @Expose
    @SerializedName("StatusName")
    private String statusName;

    public int getIsSuccess() {
        return isSuccess;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
