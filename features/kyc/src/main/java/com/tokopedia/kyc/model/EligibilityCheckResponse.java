package com.tokopedia.kyc.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class EligibilityCheckResponse {

    @SerializedName("errors")
    List<Map<String, String>> errors;

    @SerializedName("kyc_request_id")
    private int kycRequestId = -1;

    public int getKycRequestId() {
        return kycRequestId;
    }

    public void setKycRequestId(int kycRequestId) {
        this.kycRequestId = kycRequestId;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }
}
