package com.tokopedia.kyc.model;

import com.google.gson.annotations.SerializedName;

public class KYCDocumentUploadResponse {
    @SerializedName("code")
    private String code;
    @SerializedName("data")
    KycImageUploadDataClass kycImageUploadDataClass;
    @SerializedName("message")
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public KycImageUploadDataClass getKycImageUploadDataClass() {
        return kycImageUploadDataClass;
    }

    public void setKycImageUploadDataClass(KycImageUploadDataClass kycImageUploadDataClass) {
        this.kycImageUploadDataClass = kycImageUploadDataClass;
    }
}
