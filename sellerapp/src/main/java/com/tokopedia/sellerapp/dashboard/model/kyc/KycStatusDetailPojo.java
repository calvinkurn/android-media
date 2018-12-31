package com.tokopedia.sellerapp.dashboard.model.kyc;

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
}
