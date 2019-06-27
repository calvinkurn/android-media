package com.tokopedia.kyc.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class KycImageUploadDataClass {

    @SerializedName("document_type")
    private String documentType;
    @SerializedName("document_id")
    private int documentId = -1;
    @SerializedName("kyc_request_id")
    private int kycRequestId = -1;
    @SerializedName("errors")
    private List<Map<String, String>> errors;

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getKycRequestId() {
        return kycRequestId;
    }

    public void setKycRequestId(int kycRequestId) {
        this.kycRequestId = kycRequestId;
    }
}
