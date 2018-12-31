package com.tokopedia.user_identification_common.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 12/11/18.
 */
public class GetApprovalStatusPojo {

    @Expose
    @SerializedName("kycStatus")
    private KycStatusPojo kycStatus;

    public KycStatusPojo getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatusPojo kycStatus) {
        this.kycStatus = kycStatus;
    }
}
